package org.thoth.imaging;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class DateTimeOriginalWriter {

    protected File jpegImageFile;

    public DateTimeOriginalWriter(File jpegImageFile) {
        this.jpegImageFile = jpegImageFile;
    }

    /**
     * Write date time orginal exif tag to image
     *
     * @param dst Write to this file
     * @param calendar
     */
    public void write(final File dst, Calendar calendar)
        throws IOException, ImageReadException, ImageWriteException {

        try (FileOutputStream fos = new FileOutputStream(dst);
            OutputStream os = new BufferedOutputStream(fos);) {

            TiffOutputSet outputSet = null;

            // note that metadata might be null if no metadata is found.
            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                // note that exif might be null if no Exif metadata is found.
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    // TiffImageMetadata class is immutable (read-only).
                    // TiffOutputSet class represents the Exif data to write.
                    //
                    // Usually, we want to update existing Exif metadata by
                    // changing
                    // the values of a few fields, or adding a field.
                    // In these cases, it is easiest to use getOutputSet() to
                    // start with a "copy" of the fields read from the image.
                    outputSet = exif.getOutputSet();
                }
            }

            // if file does not contain any exif metadata, we create an empty
            // set of exif metadata. Otherwise, we keep all of the other
            // existing tags.
            if (null == outputSet) {
                System.out.printf("Creating new TiffOutputSet%n");
                outputSet = new TiffOutputSet();
                outputSet.addRootDirectory();
                outputSet.addExifDirectory();
            }

            TiffOutputDirectory exifDirectory = outputSet
                .getExifDirectory();

            exifDirectory
                .removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
            exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL,
                String.format("%d:%02d:%02d %02d:%02d:%02d"
                    , calendar.get(Calendar.YEAR)
                    , calendar.get(Calendar.MONTH) + 1
                    , calendar.get(Calendar.DAY_OF_MONTH)
                    , calendar.get(Calendar.HOUR_OF_DAY)
                    , calendar.get(Calendar.MINUTE)
                    , calendar.get(Calendar.SECOND)
                )
            );

//            exifDirectory
//                .removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
//            exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED,
//                "2016:02:02 02:00:00");

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
                outputSet);
        }
    }
}
