package com.bonait.bnframework.common.utils;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by LY on 2019/1/24.
 * 文件类型工具类
 */
public class MediaFileUtils {
    //音频文件类型
    public static final int FILE_TYPE_MP3     = 1;
    public static final int FILE_TYPE_M4A     = 2;
    public static final int FILE_TYPE_WAV     = 3;
    public static final int FILE_TYPE_AMR     = 4;
    public static final int FILE_TYPE_AWB     = 5;
    public static final int FILE_TYPE_WMA     = 6;
    public static final int FILE_TYPE_OGG     = 7;
    public static final int FILE_TYPE_AAC     = 8;
    public static final int FILE_TYPE_MKA     = 9;
    public static final int FILE_TYPE_FLAC    = 10;
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_FLAC;

    // MIDI文件类型
    public static final int FILE_TYPE_MID     = 11;
    public static final int FILE_TYPE_SMF     = 12;
    public static final int FILE_TYPE_IMY     = 13;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

    //视频文件类型
    public static final int FILE_TYPE_MP4     = 21;
    public static final int FILE_TYPE_M4V     = 22;
    public static final int FILE_TYPE_3GPP    = 23;
    public static final int FILE_TYPE_3GPP2   = 24;
    public static final int FILE_TYPE_WMV     = 25;
    public static final int FILE_TYPE_ASF     = 26;
    public static final int FILE_TYPE_MKV     = 27;
    public static final int FILE_TYPE_MP2TS   = 28;
    public static final int FILE_TYPE_AVI     = 29;
    public static final int FILE_TYPE_WEBM    = 30;
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4;
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_WEBM;

    //更多视频文件类型
    public static final int FILE_TYPE_MP2PS   = 200;
    private static final int FIRST_VIDEO_FILE_TYPE2 = FILE_TYPE_MP2PS;
    private static final int LAST_VIDEO_FILE_TYPE2 = FILE_TYPE_MP2PS;

    //图像文件类型
    public static final int FILE_TYPE_JPEG    = 31;
    public static final int FILE_TYPE_GIF     = 32;
    public static final int FILE_TYPE_PNG     = 33;
    public static final int FILE_TYPE_BMP     = 34;
    public static final int FILE_TYPE_WBMP    = 35;
    public static final int FILE_TYPE_WEBP    = 36;
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WEBP;

    // Playlist file types
    public static final int FILE_TYPE_M3U      = 41;
    public static final int FILE_TYPE_PLS      = 42;
    public static final int FILE_TYPE_WPL      = 43;
    public static final int FILE_TYPE_HTTPLIVE = 44;
    private static final int FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U;
    private static final int LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_HTTPLIVE;
    // Drm文件类型
    public static final int FILE_TYPE_FL      = 51;
    private static final int FIRST_DRM_FILE_TYPE = FILE_TYPE_FL;
    private static final int LAST_DRM_FILE_TYPE = FILE_TYPE_FL;
    //其他流行的文件类型
    public static final int FILE_TYPE_TEXT          = 100;
    public static final int FILE_TYPE_HTML          = 101;
    public static final int FILE_TYPE_PDF           = 102;
    public static final int FILE_TYPE_XML           = 103;
    public static final int FILE_TYPE_MS_WORD       = 104;
    public static final int FILE_TYPE_MS_EXCEL      = 105;
    public static final int FILE_TYPE_MS_POWERPOINT = 106;
    public static final int FILE_TYPE_MS_DOT        = 108;
    public static final int FILE_TYPE_WPS_DOCX      = 109;
    public static final int FILE_TYPE_WPS_DOTX      = 110;
    public static final int FILE_TYPE_WPS_XLSX      = 111;
    public static final int FILE_TYPE_WPS_XLTX      = 112;
    public static final int FILE_TYPE_WPS_PPTX      = 113;
    public static final int FILE_TYPE_WPS_POTX      = 114;
    public static final int FILE_TYPE_WPS_PPSX      = 115;
    private static final int FIRST_DOC_FILE_TYPE = FILE_TYPE_TEXT;
    private static final int LAST_DOC_FILE_TYPE = FILE_TYPE_WPS_PPSX;

    public static final int FILE_TYPE_ZIP           = 300;
    public static final int FILE_TYPE_RAR           = 301;
    private static final int FIRST_COMPRESSION_FILE_TYPE = FILE_TYPE_ZIP;
    private static final int LAST_COMPRESSION_FILE_TYPE = FILE_TYPE_RAR;


    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    private static final HashMap<String, MediaFileType> sFileTypeMap
            = new HashMap<String, MediaFileType>();
    private static final HashMap<String, Integer> sMimeTypeMap
            = new HashMap<String, Integer>();

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, fileType);
    }
    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("MPGA", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");
        addFileType("OGG", FILE_TYPE_OGG, "audio/ogg");
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");
        addFileType("OGA", FILE_TYPE_OGG, "application/ogg");
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac");
        addFileType("AAC", FILE_TYPE_AAC, "audio/aac-adts");
        addFileType("MKA", FILE_TYPE_MKA, "audio/x-matroska");

        addFileType("MID", FILE_TYPE_MID, "audio/midi");
        addFileType("MIDI", FILE_TYPE_MID, "audio/midi");
        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");
        addFileType("RTX", FILE_TYPE_MID, "audio/midi");
        addFileType("OTA", FILE_TYPE_MID, "audio/midi");
        addFileType("MXMF", FILE_TYPE_MID, "audio/midi");

        addFileType("MPEG", FILE_TYPE_MP4, "video/mpeg");
        addFileType("MPG", FILE_TYPE_MP4, "video/mpeg");
        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("MKV", FILE_TYPE_MKV, "video/x-matroska");
        addFileType("WEBM", FILE_TYPE_WEBM, "video/webm");
        addFileType("TS", FILE_TYPE_MP2TS, "video/mp2ts");
        addFileType("AVI", FILE_TYPE_AVI, "video/avi");
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv");
        addFileType("ASF", FILE_TYPE_ASF, "video/x-ms-asf");

        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("GIF", FILE_TYPE_GIF, "image/gif");
        addFileType("PNG", FILE_TYPE_PNG, "image/png");
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp");
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");
        addFileType("WEBP", FILE_TYPE_WEBP, "image/webp");

        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl");
        addFileType("M3U", FILE_TYPE_M3U, "application/x-mpegurl");
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls");
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl");
        addFileType("M3U8", FILE_TYPE_HTTPLIVE, "application/vnd.apple.mpegurl");
        addFileType("M3U8", FILE_TYPE_HTTPLIVE, "audio/mpegurl");
        addFileType("M3U8", FILE_TYPE_HTTPLIVE, "audio/x-mpegurl");
        addFileType("FL", FILE_TYPE_FL, "application/x-android-drm-fl");

        addFileType("TXT", FILE_TYPE_TEXT, "text/plain");
        addFileType("HTM", FILE_TYPE_HTML, "text/html");
        addFileType("HTML", FILE_TYPE_HTML, "text/html");
        addFileType("PDF", FILE_TYPE_PDF, "application/pdf");
        addFileType("XML", FILE_TYPE_XML, "text/xml");
        addFileType("DOC", FILE_TYPE_MS_WORD, "application/msword");
        addFileType("DOT", FILE_TYPE_MS_DOT, "application/msword");
        addFileType("DOCX", FILE_TYPE_WPS_DOCX, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileType("DOTX", FILE_TYPE_WPS_DOTX, "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        addFileType("XLSX", FILE_TYPE_WPS_XLSX, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        addFileType("XLTX", FILE_TYPE_WPS_XLTX, "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        addFileType("PPTX", FILE_TYPE_WPS_PPTX, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        addFileType("POTX", FILE_TYPE_WPS_POTX, "application/vnd.openxmlformats-officedocument.presentationml.template");
        addFileType("PPSX", FILE_TYPE_WPS_PPSX, "application/vnd.openxmlformats-officedocument.presentationml.slideshow");

        addFileType("XLS", FILE_TYPE_MS_EXCEL, "application/vnd.ms-excel");
        addFileType("PPT", FILE_TYPE_MS_POWERPOINT, "application/mspowerpoint");

        addFileType("ZIP", FILE_TYPE_ZIP, "application/zip");
        addFileType("RAR", FILE_TYPE_RAR, "application/rar");

        addFileType("FLAC", FILE_TYPE_FLAC, "audio/flac");
        addFileType("MPG", FILE_TYPE_MP2PS, "video/mp2p");
        addFileType("MPEG", FILE_TYPE_MP2PS, "video/mp2p");
    }

    /**
     * check is audio or not
     * @param fileType file type integer value
     * @return if is audio type , return true;otherwise , return false
     */
    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE));
    }

    /**
     * check is video or not
     * @param fileType file type integer value
     * @return if is video type , return true ; otherwise , return false
     */
    public static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE)
                || (fileType >= FIRST_VIDEO_FILE_TYPE2 &&
                fileType <= LAST_VIDEO_FILE_TYPE2);
    }

    /**
     * check is image or not
     * @param fileType file type integer value
     * @return if is image type , return true ; otherwise , return false ;
     */
    public static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);
    }

    /**
     * check is playlist or not
     * @param fileType file type integer value
     * @return if is playlist type , return true ; otherwise , return false ;
     */
    public static boolean isPlayListFileType(int fileType) {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE);
    }

    /**
     * check is drm or not
     * @param fileType file type integer value
     * @return if is drm type , return true ; otherwise , return false ;
     */
    public static boolean isDrmFileType(int fileType) {
        return (fileType >= FIRST_DRM_FILE_TYPE &&
                fileType <= LAST_DRM_FILE_TYPE);
    }

    /**
     * 检查是否是文本类型
     * */
    public static boolean isWordFileType(int fileType) {
        return (fileType >= FIRST_DOC_FILE_TYPE &&
                fileType <= LAST_DOC_FILE_TYPE);
    }

    /**
     * 检查是否是压缩文件类型
     * */
    public static boolean isCompressionFileType(int fileType) {
        return (fileType >= FIRST_COMPRESSION_FILE_TYPE &&
                fileType <= LAST_COMPRESSION_FILE_TYPE);
    }

    /**
     * get file's extension by file' path 按文件路径获取文件的扩展名
     * @param path file's path
     * @return MediaFileType if the given file extension exist , or null
     */
    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf('.');
        if (lastDot < 0)
            return null;
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(Locale.ROOT));
    }

    /**
     * 按后缀名返回MediaFileType
     *
     * @param suffix 文件后缀名
     *
     * @return MediaFileType if the given file extension exist , or null
     *
     * */
    public static MediaFileType getFileTypeBySuffix(String suffix) {

        // toUpperCase(Locale.ROOT) 转换为大写字符串
        return sFileTypeMap.get(suffix.toUpperCase(Locale.ROOT));
    }

    public static String getMimeTypeBySuffix(String suffix) {
        MediaFileType mediaFileType = sFileTypeMap.get(suffix.toUpperCase(Locale.ROOT));
        return (mediaFileType == null ? null : mediaFileType.mimeType);
    }

    /**
     * check the given mime type is mime type media or not 检查给定的mime类型是否是mime类型的媒体
     * @param mimeType mime type to check
     * @return if the given mime type is mime type media,return true ;otherwise , false
     */
    public static boolean isMimeTypeMedia(String mimeType) {
        int fileType = getFileTypeForMimeType(mimeType);
        return isAudioFileType(fileType) || isVideoFileType(fileType)
                || isImageFileType(fileType) || isPlayListFileType(fileType);
    }

    /**
     * generates a title based on file name
     * @param path file's path
     *
     * @return file'name without extension
     */
    public static String getFileTitle(String path) {
        // extract file name after last slash
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash >= 0) {
            lastSlash++;
            if (lastSlash < path.length()) {
                path = path.substring(lastSlash);
            }
        }
        // truncate the file extension (if any)
        int lastDot = path.lastIndexOf('.');
        if (lastDot > 0) {
            path = path.substring(0, lastDot);
        }
        return path;
    }

    /**
     * get mine type integer value 根据mime类型获取mime类型整数值
     * @param mimeType mime type to get
     * @return return mime type value if exist ;or zero value if not exist
     */
    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value.intValue());
    }

    /**
     * get file's mime type base on path 根据文件路径获取文件的mime类型
     * @param path file path
     * @return return mime type if exist , or null
     */
    public static String getMimeTypeForFile(String path) {
        MediaFileType mediaFileType = getFileType(path);
        return (mediaFileType == null ? null : mediaFileType.mimeType);
    }
}
