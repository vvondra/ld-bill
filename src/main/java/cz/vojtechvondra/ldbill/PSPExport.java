package cz.vojtechvondra.ldbill;


import java.io.*;

/**
 * Represents a export file from the psp.cz website
 * Files are CSV-like plaintext files encoded in cp1250 and separated with a vertical bar
 * The provided output is a sanitized, decoded and separated list of cell values for each row
 */
public class PSPExport {

    private PSPDownloader dataDownloader;
    private String name;
    private BufferedReader in;

    /**
     * @param dataDownloader used to access the export file
     * @param name dataset name
     */
    public PSPExport(PSPDownloader dataDownloader, String name) {
        this.dataDownloader = dataDownloader;
        this.name = name;
    }

    /**
     * Prepares the file to be exported from and downloads it if necessary
     * @throws IOException
     */
    public void prepare() throws IOException {
        File exportFile = dataDownloader.getDataset(name);
        in = new BufferedReader(new InputStreamReader( new FileInputStream(exportFile), "Cp1250"));
    }

    /**
     * @return an array of data from the next row of the export file
     */
    public String[] getLine() {
        try {
            if (in == null) {
                prepare();
            }
            String line = in.readLine();
            if (line != null) {
                String[] parts = line.split("\\|", -1);
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                    if (parts[i].length() > 0 && parts[i].charAt(0) == '\\') {
                        parts[i] = parts[i].substring(1);
                    }
                }
                return parts;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Closes export file reader after use
     * @throws IOException
     */
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }
}
