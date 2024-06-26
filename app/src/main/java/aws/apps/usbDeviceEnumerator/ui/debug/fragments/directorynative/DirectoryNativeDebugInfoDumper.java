package aws.apps.usbDeviceEnumerator.ui.debug.fragments.directorynative;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import androidx.annotation.NonNull;
import aws.apps.usbDeviceEnumerator.ui.debug.fragments.DebugInfoDumper;

public class DirectoryNativeDebugInfoDumper implements DebugInfoDumper {

    private final String dir;

    public DirectoryNativeDebugInfoDumper(final String dir) {
        this.dir = dir;
    }

    @NonNull
    @Override
    public CharSequence dump() {
        return getDump(dir);
    }

    @SuppressWarnings({"StringConcatenationInsideStringBufferAppend"})
    private static CharSequence getDump(@NonNull final String dir) {

        final String[] command = {"ls", "-al", dir};
        final StringBuilder sb = new StringBuilder();
        sb.append("Directory '" + dir + "':\n\n");
        sb.append("Will execute: " + Arrays.toString(command) + "\n\n");

        final ProcessBuilder ps = new ProcessBuilder(command);
        ps.redirectErrorStream(true);

        BufferedReader in = null;
        try {
            final Process pr = ps.start();

            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line;
            int count = 0;
            while ((line = in.readLine()) != null) {
                if (count > 0) {
                    sb.append('\n');
                }

                sb.append(line);
                count++;
            }

            pr.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
        }

        return sb.toString();
    }

    private static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
