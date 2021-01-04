package xin.allonsy.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyExcelUtil
 *
 * @author wangshuai
 * @date 2020-04-21 14:08
 */
public class EasyExcelUtil {

    /**
     * 使用 StringList 来读取Excel
     * 列号 ： value（列号从0开始）
     *
     * @param inputStream   Excel的输入流
     * @return 返回 StringList 的列表
     */
    public static List<Map<Integer, String>> readExcelAsStringList(InputStream inputStream, String sheetName) {
        StringExcelListener listener = new StringExcelListener();
//        ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
//        excelReader.read();
        EasyExcelFactory.read(inputStream, null, listener).sheet(sheetName).headRowNumber(-1).doRead();
        return listener.getDatas();
    }

    /**
     * 从流读取excel
     *
     * @param inputStream
     * @param clazz
     * @param excelTypeEnum
     * @return
     */
    @Deprecated
    public static List<Object> readExcelWithModel(InputStream inputStream, Class<? extends BaseRowModel> clazz,
                                                  ExcelTypeEnum excelTypeEnum, int sheetNo, int headLineMun) throws IOException {
        // 解析每行结果在listener中处理
        ModelExcelListener listener;
        try {
            listener = new ModelExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
            // 默认只有一列表头
            excelReader.read(new Sheet(sheetNo, headLineMun, clazz));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return listener.getDatas();

    }

    public static <T> List<T> readExcelWithSheetName(InputStream inputStream, Class<T> clazz, String sheetName)
            throws IOException {
        try {
            CommonExcelListener<T> commonExcelListener = new CommonExcelListener<>();
            EasyExcelFactory.read(inputStream, clazz, commonExcelListener).sheet(sheetName).doRead();
            return commonExcelListener.getDatas();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 通过文件名直接读取excel
     *
     * @param filePath
     * @param headRowNumber
     * @return
     */
    public static List<Object> readExcelFromLocalFilePath(String filePath, Class<? extends BaseRowModel> clazz,
                                                          int headRowNumber) {
        ModelExcelListener listener = new ModelExcelListener();
        EasyExcel.read(filePath, clazz, listener).sheet().headRowNumber(2).doRead();
        return listener.getDatas();
    }

    public static <T> List<T> readExcelFromInputStream(InputStream inputStream, Class<T> clazz, int headRowNumber)
            throws IOException {
        CommonExcelListener<T> listener = null;
        try {
            listener = new CommonExcelListener();
            EasyExcel.read(inputStream, clazz, listener).sheet().headRowNumber(headRowNumber).doRead();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return listener.getDatas();

    }

    /**
     * 从web上读取excel
     *
     * @param multipartFile
     * @param headRowNumber
     * @return
     * @throws IOException
     */
    public static List<Object> readExcelFromMultipartFile(MultipartFile multipartFile,
                                                          Class<? extends BaseRowModel> clazz, int headRowNumber) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ModelExcelListener listener = null;
        try {
            listener = new ModelExcelListener();
            EasyExcel.read(inputStream, clazz, listener).sheet().headRowNumber(headRowNumber).doRead();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return listener.getDatas();
    }

    public static boolean isExcelFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return Lists.newArrayList("xlsx", "xls").contains(extension.toLowerCase());
    }

    /**
     * StringList 解析监听器
     */
    private static class StringExcelListener extends AnalysisEventListener {
        /**
         * 自定义用于暂时存储data
         * 可以通过实例获取该值
         *
         * 列号：value
         */
        private List<Map<Integer, String>> datas = new ArrayList<>();

        /**
         * 每解析一行都会回调invoke()方法
         *
         * @param object
         * @param context
         */
        @Override
        public void invoke(Object object, AnalysisContext context) {
            // 列号：value
            Map<Integer, String> stringMap = (LinkedHashMap<Integer, String>) object;
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            datas.add(stringMap);
            //根据自己业务做处理
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            //解析结束销毁不用的资源
            //注意不要调用datas.clear(),否则getDatas为null
        }

        public List<Map<Integer, String>> getDatas() {
            return datas;
        }

        public void setDatas(List<Map<Integer, String>> datas) {
            this.datas = datas;
        }
    }

    /**
     * 模型 解析监听器
     */
    public static class ModelExcelListener extends AnalysisEventListener {

        private List<Object> datas = new ArrayList<>();

        @Override
        public void invoke(Object object, AnalysisContext context) {
            datas.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        public List<Object> getDatas() {
            return datas;
        }

        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }

    /**
     * 模型 解析监听器
     */
    public static class CommonExcelListener<T> extends AnalysisEventListener<T> {

        private List<T> datas = new ArrayList<>();

        @Override
        public void invoke(T object, AnalysisContext context) {
            datas.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        public List<T> getDatas() {
            return datas;
        }

        public void setDatas(List<T> datas) {
            this.datas = datas;
        }
    }

}
