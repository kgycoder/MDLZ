package com.sync.app;

import java.io.File;

/** 로컬에 저장된 YouTube 오디오/영상 파일 관리 */
public final class LocalMediaStore {

    private static final String[] EXTENSIONS = {
            ".m4a", ".webm", ".opus", ".mp3", ".mp4", ".mkv", ".ogg"
    };

    private final File dir;

    public LocalMediaStore(File baseDir) {
        dir = new File(baseDir, "sync_local");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
    }

    public File getDir() {
        return dir;
    }

    public File findMediaFile(String videoId) {
        return findMediaFile(dir, videoId);
    }

    public static File findMediaFile(File dir, String videoId) {
        if (videoId == null || videoId.isEmpty()) return null;
        for (String ext : EXTENSIONS) {
            File f = new File(dir, videoId + ext);
            if (f.isFile() && f.length() > 0) return f;
        }
        File[] files = dir.listFiles();
        if (files == null) return null;
        for (File f : files) {
            if (!f.isFile() || f.length() == 0) continue;
            String name = f.getName();
            if (name.startsWith(videoId + ".") || name.equals(videoId)) return f;
        }
        return null;
    }

    public void deleteMedia(String videoId) {
        File f = findMediaFile(videoId);
        if (f != null) //noinspection ResultOfMethodCallIgnored
            f.delete();
    }

    public static String guessMimeType(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".m4a")) return "audio/mp4";
        if (name.endsWith(".mp3")) return "audio/mpeg";
        if (name.endsWith(".opus") || name.endsWith(".ogg")) return "audio/ogg";
        if (name.endsWith(".webm")) return "audio/webm";
        if (name.endsWith(".mp4") || name.endsWith(".mkv")) return "video/mp4";
        return "application/octet-stream";
    }

    public String outputTemplate(String videoId) {
        return new File(dir, videoId + ".%(ext)s").getAbsolutePath();
    }
}
