import React from "react";
import Grid from "@mui/material/Grid";
import MKBox from "components/MKBox";
import MKButton from "components/MKButton";
import MKTypography from "components/MKTypography";
import "./QuizScreen.css";
import { Slide } from "@mui/material";


// Image 
import bgImage from "assets/images/illustrations/illustration-reset.png";

const quizData = [
  {
    question: "What is Java?",
    options: [
      "A programming language",
      "An island in Indonesia",
      "A type of coffee",
      "A software development tool",
    ],
    correctAnswer: "A programming language",
  },
  {
    question: "What is the main purpose of the 'public static void main(String[] args)' method in Java?",
    options: [
      "To declare variables",
      "To execute the program",
      "To define a class",
      "To handle exceptions",
    ],
    correctAnswer: "To execute the program",
  },
  {
    question: "Which keyword is used for inheritance in Java?",
    options: ["extends", "inherits", "superclass", "implements"],
    correctAnswer: "extends",
  },
  {
    question: "What is the Java Virtual Machine (JVM)?",
    options: [
      "A hardware component",
      "A software component",
      "A programming language",
      "A database management system",
    ],
    correctAnswer: "A software component",
  },
  {
    question: "What is the purpose of the 'super' keyword in Java?",
    options: [
      "To call the parent class constructor",
      "To create a new instance of a class",
      "To access a static variable",
      "To override a method",
    ],
    correctAnswer: "To call the parent class constructor",
  },
  {
    question: "What is a constructor in Java?",
    options: [
      "A method to initialize a class",
      "A reserved keyword",
      "A control statement",
      "A data type",
    ],
    correctAnswer: "A method to initialize a class",
  },
  {
    question: "Which collection framework class is synchronized in Java?",
    options: ["ArrayList", "HashMap", "Vector", "LinkedList"],
    correctAnswer: "Vector",
  },
  {
    question: "What is the purpose of the 'final' keyword in Java?",
    options: [
      "To declare a constant",
      "To indicate the end of a program",
      "To define a class",
      "To create an immutable class",
    ],
    correctAnswer: "To declare a constant",
  },
  {
    question: "What is the use of the 'this' keyword in Java?",
    options: [
      "To call a method",
      "To create an instance of a class",
      "To refer to the current object",
      "To handle exceptions",
    ],
    correctAnswer: "To refer to the current object",
  },
  {
    question: "Which exception is thrown when a division by zero occurs in Java?",
    options: ["ArithmeticException", "NullPointerException", "ArrayIndexOutOfBoundsException", "NumberFormatException"],
    correctAnswer: "ArithmeticException",
  },
];

function QuizScreen() {

  // States 

  const [currentQuestionIndex, setCurrentQuestionIndex] = React.useState(0);
  const [userAnswers, setUserAnswers] = React.useState(new Array(quizData.length).fill(null));
  const [submitted, setSubmitted] = React.useState(false);

  // HandleNext() for changing question

  const handleNext = () => {
    setCurrentQuestionIndex((prevIndex) => prevIndex + 1);
  };

  // Handle Option Select for setting user sekection in array

  const handleOptionSelect = (optionIndex) => {
    const updatedAnswers = [...userAnswers];
    updatedAnswers[currentQuestionIndex] = optionIndex;
    setUserAnswers(updatedAnswers);
  };

  // Handle Submit for submitting test

  const handleSubmit = () => {
    setSubmitted(true);
  };

  // for getting current question

  const currentQuestion = quizData[currentQuestionIndex];

  // To calculate score 

  const calculateScore = () => {
    let score = 0;
    for (let i = 0; i < quizData.length; i++) {
      if (userAnswers[i] === quizData[i].options.indexOf(quizData[i].correctAnswer)) {
        score++;
      }
    }
    return score;
  };


  // for changing question while pressing enter key

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {

      handleNext();
    }
  };


  // To Show Result

  const renderResults = () => {
    return (
      <>
        <MKBox position="fixed" style={{ width: "100%" }} width="100%"></MKBox>
        <Grid container spacing={3} justifyContent={"center"} alignItems="center"   >

          {/* Image Container - Hidden for Results */}

          {/* {submitted ? null : (
            <Grid item xs={12} lg={6}>
              <MKBox
                display={{ xs: "none", lg: "block" }}
                width="calc(100% - 2rem)"
                height="calc(100vh - 10rem)"
                borderRadius="lg"
                ml={2}
                mt={2}
                style={{ backgroundImage: `url(${bgImage})`, backgroundRepeat: "no-repeat" }}
              />
            </Grid>
          )} */}

          {/* Question Container */}

          <Grid

            item
            xs={12}
            sm={10}
            md={8}
            lg={6}
            xl={6}
            style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
            ml={{ xs: "auto", lg: 6 }}
            mr={{ xs: "auto", lg: 6 }}
          >
            <MKTypography style={{ fontSize: "18px" }} color="text" mt={4}>
              {calculateScore() >= 5
                ? "Congratulations 🏆 You Passed the Test!"
                : "Sorry, You Failed the Test. Better luck next time!"}
            </MKTypography>

            {/* for getting all the questions , correct answer and user answer on same screen ---> map used  */}

            {quizData.map((question, index) => (
              <Slide
                key={index}
                direction="right"
                in={true}
                mountOnEnter
                unmountOnExit
                timeout={{ enter: 1600, exit: 800 }}
              >
                <MKBox
                  mt={2}
                  className="glassEffect"
                  bgColor={submitted ? (userAnswers[index] === question.options.indexOf(question.correctAnswer) ? "#45a359" : "#e64343") : "white"}
                  borderRadius="8px"
                  shadow="lg"
                  style={{ width: "100%", minWidth: "45rem" }}
                  p={2}
                  transition="background-color 0.2s ease-in" /* Added transition for smooth color change */
                >
                  <MKTypography variant="body1" style={{ color: "#1C2833" }}>
                    <strong>Question {index + 1}:</strong> {question.question}
                  </MKTypography>

                  {/* For correct selection only showing correct answer */}

                  <MKTypography variant="body1" style={{ color: "white", fontSize: "16px" }}>
                    <span>{userAnswers[index] === question.options.indexOf(question.correctAnswer) ? "Correct:" : "Correct Answer:"}</span> <strong>{question.correctAnswer}
                    </strong>
                  </MKTypography>

                  {/* applied conditon only for incorrect selected by user--->showing both correct and users selection  */}

                  {userAnswers[index] !== question.options.indexOf(question.correctAnswer) && (
                    <MKTypography variant="body1" style={{ color: "#F4D03F", fontSize: "16px" }}>
                      <span style={{ color: "white" }}>Your Answer:</span> <strong> {question.options[userAnswers[index]]}</strong>
                    </MKTypography>
                  )}
                </MKBox>
              </Slide>
            ))}
          </Grid>
        </Grid >
        <MKBox pt={6} px={1} mt={6}></MKBox>
      </>
    );
  };


  return (
    <>
      <MKBox position="fixed" width="100%"></MKBox>

      <Grid spacing={6} container justifyContent={"center"} alignItems="center">

        {/* Image Container - Hidden for Results */}


        {/* {submitted ? null : (
          <Grid item xs={12} lg={6}>
            <MKBox
              display={{ xs: "none", lg: "block" }}
              width="calc(100% - 2rem)"
              height="calc(100vh - 10rem)"
              borderRadius="lg"
              ml={2}
              mt={2}
              style={{ backgroundImage: `url(${bgImage})`, backgroundRepeat: "no-repeat" }}
            />
          </Grid>
        )} */}


        {/* Question Container */}

        <Grid
          item
          xs={12}
          sm={10}
          md={6}
          mt={submitted ? -5 : 3}
          xl={submitted ? 9 : 7.5}
          ml={{ xs: "auto", lg: 6 }}
          mr={{ xs: "auto", lg: 6 }}
        >
          <MKBox
            bgColor="white"
            borderRadius="xl"
            shadow="lg"
            display="flex"
            flexDirection="column"
            mt={{ xs: 10, sm: 10, md: 12 }}
            mb={{ xs: 10, sm: 10, md: 0 }}
            mx={3}
            xl={submitted ? 8 : ""}
          >
            <MKBox
              variant="gradient"
              bgColor="info"
              coloredShadow="info"
              borderRadius="lg"
              p={2}
              mx={2}
              mt={-3}

            >
              <MKTypography variant="h3" color="white">
                Question {currentQuestionIndex + 1}/{quizData.length}
              </MKTypography>
            </MKBox>

            {/* applied condition if submitted then only show result otherwise show questions */}

            {submitted ? (
              renderResults()
            ) : (
              <MKBox p={3}>
                <MKTypography variant="body1" style={{ fontSize: "20px" }} color="text" mb={3}>
                  {currentQuestion.question}
                </MKTypography>
                <MKBox width="100%" component="form" method="post" autoComplete="off">
                  <Grid container spacing={6}>
                    <Grid item xs={12}>
                      {currentQuestion.options.map((option, index) => (
                        <div key={index} className="custom-radio" onClick={() => handleOptionSelect(index)}>
                          <input
                            type="radio"
                            id={`option${index}`}
                            name="answer"
                            checked={userAnswers[currentQuestionIndex] === index}
                            readOnly={submitted}
                            onKeyPress={currentQuestionIndex < quizData.length - 1 ? handleKeyPress : handleSubmit}
                          />
                          <label style={{ fontSize: "18px" }} htmlFor={`option${index}`}>{option}</label>
                        </div>
                      ))}
                    </Grid>
                  </Grid>

                  {/* applied condition for showing submit button after all question viewed */}

                  <Grid container item justifyContent="center" xs={12} mt={3} mb={2}>

                    <MKButton
                      type="button"
                      variant="gradient"
                      color="info"
                      style={{ width: "8vw" }}
                      onClick={currentQuestionIndex < quizData.length - 1 ? handleNext : handleSubmit}
                    >
                      {currentQuestionIndex < quizData.length - 1 ? "Next" : "Submit"}
                    </MKButton>

                  </Grid>
                </MKBox>
              </MKBox>
            )}
          </MKBox>
        </Grid>

      </Grid>
      <MKBox pt={6} px={1} mt={6}></MKBox>
    </>
  );
}

export default QuizScreen;

