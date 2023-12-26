package connecticus.in.quiz.util;

import connecticus.in.quiz.dto.QuestionResponse;
import connecticus.in.quiz.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "question", target = "question")
    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "difficulty", target = "difficulty")
    @Mapping(source = "type", target = "type")
    @Mapping(target = "active", expression = "java(true)")
    QuestionResponse questionToQuestionResponse(Question question);
}
