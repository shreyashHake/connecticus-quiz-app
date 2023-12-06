import React, { useEffect, useState } from "react";
import Grid from "@mui/material/Grid";
import MKBox from "components/MKBox";
import MKButton from "components/MKButton";
import MKTypography from "components/MKTypography";
import "./QuizScreen.css";
import { Slide } from "@mui/material";

// Image 
import bgImage from "assets/images/illustrations/illustration-reset.png";



function QuizScreen() {

  // States 
  const [quizData, setQuizData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = React.useState(0);
  const [userAnswers, setUserAnswers] = React.useState(new Array(quizData.length).fill(null));
  const [submitted, setSubmitted] = React.useState(false);
  const [isOptionSelected, setIsOptionSelected] = React.useState(false);



  useEffect(() => {

    const fetchQuestions = async () => {
      try {
        const response = await fetch(`http://localhost:8080/question/subject/React/10`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();
        setQuizData(data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching data:', error);
        setError(error);
        setLoading(false);
      }
    };


    fetchQuestions();
  }, []);


  // HandleNext() for changing question

  const handleNext = () => {
    // Check if the user has selected an option for the current question
    if (isOptionSelected) {
      setCurrentQuestionIndex((prevIndex) => prevIndex + 1);
      setIsOptionSelected(false); // Reset to false for the next question
    } else {
      // Display an error message or handle the case where no option is selected
      console.log("Please choose an option before proceeding.");
    }
  };


  // HandleOptionSelect() for setting user selection in array

  const handleOptionSelect = (optionIndex) => {
    const updatedAnswers = [...userAnswers];
    updatedAnswers[currentQuestionIndex] = optionIndex;
    setUserAnswers(updatedAnswers);
    setIsOptionSelected(true); // Set to true when an option is selected
  };


  // Handle Submit for submitting test

  const handleSubmit = () => {
    setSubmitted(true);
  };


  if (loading || quizData.length === 0) {
    return <div style={{ display: "flex", justifyContent: "center", alignItems: "center", width: "100%", height: "90vh", marginRight: "50px" }} >
      <div class="spinner-border text-success" role="status">
      </div>
    </div >;
  }

  // for getting current question

  const currentQuestion = quizData[currentQuestionIndex];
  if (!currentQuestion) {
    return <div>Loading...</div>;
  }

  // To calculate score 

  const calculateScore = () => {
    let score = 0;
    for (let i = 0; i < quizData.length; i++) {
      if (userAnswers[i] === quizData[i].options.indexOf(quizData[i].answer)) {
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
                  bgColor={submitted ? (userAnswers[index] === question.options.indexOf(question.answer) ? "#45a359" : "#e64343") : "white"}
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
                    <span>{userAnswers[index] === question.options.indexOf(question.answer) ? "Correct:" : "Correct Answer:"}</span> <strong>{question.answer}
                    </strong>
                  </MKTypography>

                  {/* applied conditon only for incorrect selected by user--->showing both correct and users selection  */}

                  {userAnswers[index] !== question.options.indexOf(question.answer) && (
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
                      disabled={!isOptionSelected}
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

