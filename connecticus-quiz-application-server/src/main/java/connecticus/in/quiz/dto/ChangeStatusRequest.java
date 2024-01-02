package connecticus.in.quiz.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChangeStatusRequest {
    private List<Integer> idList;
    private boolean status;
}
