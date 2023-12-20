package connecticus.in.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponse {
    private Boolean pastStatus;
    private Boolean currentStatus;
}
