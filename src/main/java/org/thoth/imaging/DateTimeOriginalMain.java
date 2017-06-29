package org.thoth.imaging;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Michael Remijan mjremijan@yahoo.com @mjremijan
 */
public class DateTimeOriginalMain {

    public static void main(String[] args) throws Exception {

        GregorianCalendar calendar
                = new GregorianCalendar(2008, Calendar.MAY, 30, 10, 59, 59);

        File dir
            = new File("C:\\Users\\Michael\\Desktop\\Wedding pictures from photographer");

        File[] orgs
            = dir.listFiles(f -> f.isFile() && f.getName().endsWith(".jpg"));

        Arrays.sort(orgs, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        int cnt = 0;
        for (File org : orgs) {
            cnt++;
            System.out.printf("Processing %s%n", org.getName());

            File dst
                = new File(
                    "C:\\Users\\Michael\\Desktop\\Wedding pictures from photographer (reordered)"
                    , String.format("%s_reordered.jpg" ,org.getName().substring(0, org.getName().length() - 4)));

            DateTimeOriginalWriter
                writer = new DateTimeOriginalWriter(org);

            calendar.add(Calendar.SECOND, 30);
            writer.write(dst, calendar);

//            while (false == dst.renameTo(org)) {
//                System.out.printf("renameing...%n");
//            }
        }
        System.out.printf("Processed %d%n", cnt);


        System.out.printf("DONE%n");
    }
}
