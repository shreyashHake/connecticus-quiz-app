package connecticus.in.quiz.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteRequest {
    private List<Integer> idList;
    public DeleteRequest() {

    }

    public DeleteRequest(List<Integer> idList) {
        this.idList = idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }
}
