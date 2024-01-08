package connecticus.in.quiz.dto;

import java.util.List;


public class DeleteRequest {
    private List<Integer> idList;
    public DeleteRequest() {

    }

    public DeleteRequest(List<Integer> idList) {
        this.idList = idList;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }
}
