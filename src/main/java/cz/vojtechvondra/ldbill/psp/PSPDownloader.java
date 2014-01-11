package cz.vojtechvondra.ldbill.psp;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Set;

/**
 * Handles downloading and extracting datasets from the psp.cz official website
 */
public class PSPDownloader {

    /**
     * File extension of database exports in PSP archives
     */
    private static final String FILE_EXT = ".unl";

    /**
     * URLs of known export files
     */
    private static final EnumMap<ExportFiles, String> exportFiles;

    static
    {
        exportFiles = new EnumMap<>(ExportFiles.class);
        exportFiles.put(ExportFiles.DEPUTIES, "http://www.psp.cz/eknih/cdrom/opendata/poslanci.zip");
        exportFiles.put(ExportFiles.PARLIAMENTARY_PRESS, "http://www.psp.cz/eknih/cdrom/opendata/tisky.zip");
        exportFiles.put(ExportFiles.VOTE_2002, "http://www.psp.cz/eknih/cdrom/opendata/hl-2002ps.zip");
        exportFiles.put(ExportFiles.VOTE_2006, "http://www.psp.cz/eknih/cdrom/opendata/hl-2006ps.zip");
        exportFiles.put(ExportFiles.VOTE_2010, "http://www.psp.cz/eknih/cdrom/opendata/hl-2010ps.zip");
        exportFiles.put(ExportFiles.VOTE_2013, "http://www.psp.cz/eknih/cdrom/opendata/hl-2013ps.zip");
    }

    /**
     * A map from dataset names to the export file archive which it is contained in
     */
    private static final HashMap<String, ExportFiles> setsToFilesMapping;

    static {
        setsToFilesMapping = new HashMap<>();
        setsToFilesMapping.put("organy", ExportFiles.DEPUTIES);
        setsToFilesMapping.put("osoby", ExportFiles.DEPUTIES);
        setsToFilesMapping.put("poslanec", ExportFiles.DEPUTIES);
        setsToFilesMapping.put("zarazeni", ExportFiles.DEPUTIES);
        setsToFilesMapping.put("tisky", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("tisky_za", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("hist", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("stavy", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("typ_stavu", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("typ_akce", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("prechody", ExportFiles.PARLIAMENTARY_PRESS);
        setsToFilesMapping.put("hl2002s", ExportFiles.VOTE_2002);
        setsToFilesMapping.put("hl2006s", ExportFiles.VOTE_2006);
        setsToFilesMapping.put("hl2010s", ExportFiles.VOTE_2010);
        setsToFilesMapping.put("hl2013s", ExportFiles.VOTE_2013);
        /*setsToFilesMapping.put("hl2002h1", ExportFiles.VOTE_2002);
        setsToFilesMapping.put("hl2002h2", ExportFiles.VOTE_2002);
        setsToFilesMapping.put("hl2006h1", ExportFiles.VOTE_2006);
        setsToFilesMapping.put("hl2006h2", ExportFiles.VOTE_2006);
        setsToFilesMapping.put("hl2010h1", ExportFiles.VOTE_2010);
        setsToFilesMapping.put("hl2010h2", ExportFiles.VOTE_2010);
        setsToFilesMapping.put("hl2013h1", ExportFiles.VOTE_2013);*/
    }

    /**
     * @return datasets mapped to export files known to the downloader
     */
    public static Set<String> getKnownDatasetNames() {
        return setsToFilesMapping.keySet();
    }

    /**
     * Downloads the appropriate ZIP file from the psp.cz website
     * @param file package file to download (may contain multiple datasets)
     * @return a resource representing the fetched archive
     * @throws IOException
     */
    private File downloadArchive(ExportFiles file) throws IOException {
        URL website = new URL(exportFiles.get(file));
        File tempDir = getTempDir();
        File tmpFile = new File(tempDir.getAbsolutePath() + "/" + website.getPath().substring(website.getPath().lastIndexOf("/")));
        if (tmpFile.exists()) {
            return tmpFile;
        }
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(tmpFile);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24); // 16 MB should be a safe limit
        return tmpFile;
    }

    /**
     * Returns a File reference to a temporary directory, the directory is created if necessary
     *
     * @return temporary directory reserved for the application
     * @throws IOException
     */
    private File getTempDir() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir") + "/ldbill");
        if (!tempDir.exists()) {
            if (!tempDir.mkdir()) {
                throw new IOException("Could not create archive cache directory.");
            }
        }
        return tempDir;
    }

    /**
     * Returns a resource to a downloaded and extracted dataset from the psp.cz website
     * @param setName name of the dataset (UNL file)
     * @return dataset resource file
     * @throws IOException
     */
    public File getDataset(String setName) throws IOException {
        if (!setsToFilesMapping.containsKey(setName)) {
            throw new IllegalArgumentException("Unknown dataset name: " + setName);
        }

        ZipFile zf;
        File tmpFile;
        File archive = downloadArchive(setsToFilesMapping.get(setName));
        try {
            zf = new ZipFile(archive);
            tmpFile = getTempDir();
            zf.extractFile(setName + FILE_EXT, tmpFile.getAbsolutePath());
        } catch (ZipException e) {
            throw new IOException("Could not extract dataset archive");
        }

        return new File(tmpFile + File.separator + setName + FILE_EXT);
    }

    /**
     * Known archives published on the PSP.cz website
     */
    protected enum ExportFiles {
        DEPUTIES,
        PARLIAMENTARY_PRESS,
        VOTE_2013,
        VOTE_2010,
        VOTE_2006,
        VOTE_2002
    }

}
