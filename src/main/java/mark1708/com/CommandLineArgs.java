package mark1708.com;

import com.beust.jcommander.Parameter;

class CommandLineArgs {
    @Parameter(
            names = {"--src", "-s"},
            description = "Path to source image/directory",
            required = true
    )
    private String srcPath;
    @Parameter(
            names = {"--dst", "-d"},
            description = "Path to destination directory (by default image(s) will be saved in src directory)",
            required = false
    )
    private String dstPath = "";
    @Parameter(
            names = {"--size", "-S"},
            description = "New image size (by default the image will be resized to size 300px)",
            required = false
    )
    private int newSize = 300;
    @Parameter(
            names = {"--mode", "-m"},
            description = "Mode of resizing image(1 - resize by nearest neighbor algorithm; 2 - resize by imgscalr; 3 - resize by Graphics2D with RenderingHints)",
            required = false
    )
    private int mode = 3;
    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help = false;

    public String getSrcPath() {
        return srcPath;
    }

    public String getDstPath() {
        return dstPath;
    }

    public int getNewSize() {
        return newSize;
    }

    public int getMode() {
        return mode;
    }

    public boolean isHelp() {
        return help;
    }
}

