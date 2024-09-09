package BVK.GlobalMethod.Utils;



import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import static BVK.GlobalMethod.AllureReport.AllureAttachments.attachVideoToAllure;
import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecord extends ScreenRecorder{

    private static ScreenRecorder screenRecorder;
    private static String name;
    private static String filepath = "src/test/resources/test-recordings/";
    private static File file;
    private static File fileWebm;
    private static File fileAvi;

    public ScreenRecord(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                              Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {

        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        return new File(movieFolder,
                name + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String methodName) throws Exception {
        System.setProperty("java.awt.headless", "false");

        file = new File(filepath);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice()
                .getDefaultConfiguration();
        screenRecorder = new ScreenRecord(gc, captureSize,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file, methodName);
        screenRecorder.start();
    }

    public static void stopRecording(boolean enable, boolean attachToAllure, boolean deleteVideo) throws Exception
    {
        screenRecorder.stop();
        if(enable){
            if(attachToAllure){
                fileWebm = new File(filepath+name+".mp4");
                convertAviToMp4();
                attachVideoToAllure(fileWebm.getPath());
                fileWebm.delete();
            }
            if(deleteVideo) {
                fileAvi = new File(filepath+name+".avi");
                fileAvi.delete();
            }
        }


    }

    public static void convertAviToMp4() throws FileNotFoundException {
        File source = new File(filepath+name+".avi");
        File target = new File(filepath+name+".mp4");
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        // Here 160 kbps video is 160000
        video.setBitRate(320000);
        // More the frames more quality and size, but keep it low based on devices like mobile
        video.setFrameRate(30);
//        video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);
//        video.setSize(new VideoSize(1920, 1200));
        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setOutputFormat("webm");
        attrs.setOutputFormat("mp4");
        attrs.setVideoAttributes(video);

        try {
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (Exception e) {
            /*Handle here the video failure*/
            e.printStackTrace();
        }


    }

    public static void convertAviToWebm() {
        File source = new File(filepath+name+".avi");
        File target = new File(filepath+name+".webm");
        VideoAttributes video = new VideoAttributes();
        video.setCodec("libvpx-vp9");
//        video.setX264Profile(X264_PROFILE.BASELINE);
        // Here 160 kbps video is 160000
        video.setBitRate(160000);
        // More the frames more quality and size, but keep it low based on devices like mobile
        video.setFrameRate(15);
//        video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);
        video.setSize(new VideoSize(1920, 1080));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("webm");
        attrs.setVideoAttributes(video);
        try {
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (Exception e) {
            /*Handle here the video failure*/
            e.printStackTrace();
        }
    }

}
