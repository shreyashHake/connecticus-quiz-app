package connecticus.in.quiz.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteRequest {
    private List<Integer> idList;

    public DeleteRequest(List<Integer> idList) {
        this.idList = idList;
    }
}
