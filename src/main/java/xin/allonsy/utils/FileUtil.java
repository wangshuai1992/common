package xin.allonsy.utils;

import java.io.*;
import java.nio.file.Files;

/**
 * FileUtil
 *
 * @author wangshuai
 * @version V1.0
 * @date 2020-05-20 20:07
 */
public class FileUtil {

    public static String readFile(String url) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            String temp;
            while ((temp = br.readLine()) != null) {
                builder.append(temp);
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    public static String readFile(InputStream inputStream) {
        try {
            StringBuilder builder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String temp;
                while ((temp = br.readLine()) != null) {
                    builder.append(temp);
                    builder.append('\n');
                }
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException("read file failed.");
        }
    }

    public static void createOrOverwriteFile(String url, String content) throws IOException {
        System.out.println(url);
        File file = new File(url);
        if (file.exists()) {
            Files.delete(file.toPath());
        }
        if (!file.createNewFile()) {
            System.out.println("创建文件失败");
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

}
