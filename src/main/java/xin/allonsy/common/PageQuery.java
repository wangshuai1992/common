package xin.allonsy.common;

import java.util.Collections;
import java.util.List;

/**
 * 分页查询
 *
 * @author wangshuai
 */
public class PageQuery<T> extends BaseQuery {

    private static final long serialVersionUID = -6584732412081090793L;

    /**
     * 执行所有查询语句后返回的结果集
     */
    protected List<T> dataList;

    /**
     * 默认每页显示的记录数
     */
    public static final Integer DEFAULT_PAGE_SIZE = 50;

    /**
     * 一页大小
     */
    protected Integer pageSize = 50;

    /**
     * 起始位置
     */
    protected Integer startPos = 0;

    /**
     * 总记录数
     */
    protected Integer totalCount = 0;

    /**
     * 当前页数，从 1开始，1代表第一页
     */
    protected Integer pageNo = 1;

    /**
     * 总页数
     */
    protected Integer totalPage = 0;

    /**
     * 获取一页的记录数
     *
     * @return
     */
    public Integer getPageSize() {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    /**
     * pageNo
     *
     * @param pageNo
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return the pageNo
     */
    public Integer getPageNo() {
        if (pageNo < 1) {
            pageNo = 1;
        }
        return pageNo;
    }

    /**
     * pageSize
     *
     * @param pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 是否有下页
     *
     * @return
     */
    public Boolean hasNextPage() {
        return pageNo < getTotalPage();
    }

    /**
     * 获取当前页的最后一行
     *
     * @return
     */
    public Integer getEndPos() {
        if (getPageNo() * getPageSize() < getTotalCount()) {
            return getPageNo() * getPageSize();
        } else {
            return getTotalCount();
        }
    }

    /**
     * @return the dataList
     */
    public List<T> getDataList() {
        if (dataList == null) {
            return Collections.emptyList();
        } else {
            return dataList;
        }
    }

    /**
     * @param dataList the dataList to set
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * @return the startPos
     */
    public Integer getStartPos() {
        return (getPageNo() - 1) * getPageSize();
    }

    /**
     * @param startPos the startPos to set
     */
    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    /**
     * @return the totalRecord
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Integer totalCount) {
        this.totalPage = (totalCount + getPageSize() - 1) / getPageSize();
        this.totalCount = totalCount;
    }

    /**
     * @return the totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage the totalPage to set
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
