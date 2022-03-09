package mark1708.com;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    private static String srcPath;
    private static String dstPath;
    private static int size;
    private static List<Thread> threads = new ArrayList<>();
    private static int mode;

    public static void main(String[] args) {
        getInputData(args);
        File src = new File(srcPath);
        List<File> files = getFiles(src);

        try {
            int processorsCount = Runtime.getRuntime().availableProcessors();
            logger.info("Cores involved: " + processorsCount);

            int filesPerThread = files.size() / processorsCount;
            int oneMoreSizeCount = files.size() % processorsCount;
            int bound = oneMoreSizeCount * (filesPerThread + 1);

            List<File> subListOneMore = files.subList(0, bound);
            List<File> subListStandard = files.subList(bound, files.size());

            for (int i = 0; i < processorsCount; i++) {
                int lowBound = (i < oneMoreSizeCount) ? i * (filesPerThread + 1) : (i - oneMoreSizeCount) * filesPerThread;
                int upBound = (i < oneMoreSizeCount) ? lowBound + filesPerThread + 1 : lowBound + filesPerThread;

                List<File> perThread = (i < oneMoreSizeCount) ? subListOneMore.subList(lowBound, upBound) : subListStandard.subList(lowBound, upBound);
                ImageResizer imageResizer = new ImageResizer(perThread, size, dstPath, mode);

                Thread thread = new Thread(imageResizer);
                thread.start();
                threads.add(thread);
            }
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {
            logger.error("An error has occurred:" + e);
            System.err.println("Check the correctness of the entered data");
            System.exit(-1);
        }

        System.out.println("Duration: " + ImageResizer.duration);
        System.out.println("Count of objects: " + files.size());
    }

    private static List<File> getFiles(File src) {
        List<File> files = src.isDirectory() ? Arrays.asList(Objects.requireNonNull(src.listFiles())) : List.of(src);

        return files.stream()
                .filter(file -> !file.isDirectory())
                .filter(ImageResizer::isAvailableExtension)
                .sorted(Comparator.comparing(File::length)).collect(Collectors.toList());
    }

    private static void getInputData(String[] args) {
//        logger.info("Reading args from CommandLineArgs");
        CommandLineArgs parameters = new CommandLineArgs();
        JCommander commander = JCommander.newBuilder()
                .addObject(parameters)
                .build();
        try {
            commander.parse(args);
        } catch (CommandLine.ParameterException parEx) {
            logger.error("An error occurred while entering parameters:" + parEx);
            badArgsExit();
        }
        if (parameters.isHelp()) {
            commander.usage();
            System.exit(0);
        }
        srcPath = parameters.getSrcPath();
        dstPath = parameters.getDstPath();
        size = parameters.getNewSize();
        mode = parameters.getMode();

        File src = new File(srcPath);
        if (dstPath.equals("")) {
            if (src.isDirectory()) {
                dstPath = srcPath;
            } else if (src.isFile()) {
                dstPath = src.getParentFile().getAbsolutePath();
            } else {
                System.exit(-1);
            }
        }
    }

    private static void badArgsExit() {
        System.err.println("Invalid parameter. Program call example:");
        System.err.println("java -jar ImageResizer.jar -s source -d target -S 300");
        System.exit(-1);
    }

}
