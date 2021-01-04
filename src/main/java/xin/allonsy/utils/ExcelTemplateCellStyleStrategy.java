package xin.allonsy.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * excel样式策略
 *
 * @date 2019-11-14
 */
public class ExcelTemplateCellStyleStrategy extends AbstractCellStyleStrategy {

    /**
     * 默认表头样式
     */
    private WriteCellStyle defaultHeadWriteCellStyle;
    /**
     * 默认正文样式
     */
    private WriteCellStyle defaultContentWriteCellStyle;
    /**
     * 特殊行样式
     */
    private Map<Integer, WriteCellStyle> rowSpecialWriteCellStyleMap;
    /**
     * 特殊列样式
     */
    private Map<Integer, WriteCellStyle> colSpecialWriteCellStyleMap;

    /**
     * 转换为CellStyle类型
     */
    private CellStyle defaultHeadCellStyle;
    private CellStyle defaultContentCellStyle;
    private Map<Integer, CellStyle> rowSpecialCellStyleMap;
    private Map<Integer, CellStyle> colSpecialCellStyleMap;

    public ExcelTemplateCellStyleStrategy(WriteCellStyle defaultHeadWriteCellStyle,
                                          WriteCellStyle defaultContentWriteCellStyle,
                                          Map<Integer, WriteCellStyle> rowSpecialWriteCellStyleMap,
                                          Map<Integer, WriteCellStyle> colSpecialWriteCellStyleMap) {
        this.defaultHeadWriteCellStyle = defaultHeadWriteCellStyle;
        this.defaultContentWriteCellStyle = defaultContentWriteCellStyle;
        this.rowSpecialWriteCellStyleMap = rowSpecialWriteCellStyleMap;
        this.colSpecialWriteCellStyleMap = colSpecialWriteCellStyleMap;
    }

    @Override
    protected void initCellStyle(Workbook workbook) {
        this.defaultHeadCellStyle = StyleUtil.buildHeadCellStyle(workbook, defaultHeadWriteCellStyle);
        this.defaultContentCellStyle = StyleUtil.buildHeadCellStyle(workbook, defaultContentWriteCellStyle);
        if (rowSpecialWriteCellStyleMap != null) {
            this.rowSpecialCellStyleMap = rowSpecialWriteCellStyleMap.entrySet().stream().collect(Collectors
                .toMap(Map.Entry::getKey, o -> StyleUtil.buildHeadCellStyle(workbook, o.getValue()), (o1, o2) -> o1));
        }
        if (colSpecialWriteCellStyleMap != null) {
            this.colSpecialCellStyleMap = colSpecialWriteCellStyleMap.entrySet().stream().collect(Collectors
                .toMap(Map.Entry::getKey, o -> StyleUtil.buildHeadCellStyle(workbook, o.getValue()), (o1, o2) -> o1));
        }
    }

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer integer) {
        if (cell == null) {
            return;
        }

        if (colSpecialCellStyleMap != null && colSpecialCellStyleMap.get(cell.getColumnIndex()) != null) {
            cell.setCellStyle(colSpecialCellStyleMap.get(cell.getColumnIndex()));
        } else if (rowSpecialCellStyleMap != null && rowSpecialCellStyleMap.get(cell.getRowIndex()) != null) {
            cell.setCellStyle(rowSpecialCellStyleMap.get(cell.getRowIndex()));
        } else if (defaultHeadCellStyle != null) {
            cell.setCellStyle(defaultHeadCellStyle);
        }
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer integer) {
        if (cell == null) {
            return;
        }

        if (rowSpecialCellStyleMap != null && rowSpecialCellStyleMap.get(cell.getRowIndex()) != null) {
            cell.setCellStyle(rowSpecialCellStyleMap.get(cell.getRowIndex()));
        } else if (colSpecialCellStyleMap != null && colSpecialCellStyleMap.get(cell.getColumnIndex()) != null) {
            cell.setCellStyle(colSpecialCellStyleMap.get(cell.getColumnIndex()));
        } else if (defaultContentCellStyle != null) {
            cell.setCellStyle(defaultContentCellStyle);
        }
    }

}