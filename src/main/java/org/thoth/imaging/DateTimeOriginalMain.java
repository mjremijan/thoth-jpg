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
                = new GregorianCalendar(2017, Calendar.NOVEMBER, 23, 9, 17, 29);

        File srcDir
            = new File("C:\\Users\\Michael\\Desktop\\f");

        File dstDir
            = new File("C:\\Users\\Michael\\Desktop\\");

        File[] orgs
            = srcDir.listFiles(f -> f.isFile() && (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg")));

        Arrays.sort(orgs, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        int cnt = 0;
        for (File jpg : orgs) {
            cnt++;
            System.out.printf("Processing %s%n", jpg.getName());

            File dst
                = new File(
                      dstDir
                    , String.format("%s.jpg" ,jpg.getName().substring(0, jpg.getName().lastIndexOf("."))));

            DateTimeOriginalWriter
                writer = new DateTimeOriginalWriter(jpg);

            //calendar.add(Calendar.SECOND, 30);
            writer.write(dst, calendar);

//            while (false == dst.renameTo(org)) {
//                System.out.printf("renameing...%n");
//            }
        }
        System.out.printf("Processed %d%n", cnt);


        System.out.printf("DONE%n");
    }
}
