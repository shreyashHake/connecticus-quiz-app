import { useState } from "react";

// react-router-dom components
// import { Link } from "react-router-dom";

// @mui material components
import Card from "@mui/material/Card";
import Switch from "@mui/material/Switch";
import Grid from "@mui/material/Grid";
// import MuiLink from "@mui/material/Link";

// @mui icons
// import FacebookIcon from "@mui/icons-material/Facebook";
// import GitHubIcon from "@mui/icons-material/GitHub";
// import GoogleIcon from "@mui/icons-material/Google";

// Material Kit 2 React components
import MKBox from "components/MKBox";
import MKTypography from "components/MKTypography";
import MKInput from "components/MKInput";
import MKButton from "components/MKButton";

// Material Kit 2 React example components
//IF PROJECT NEEDS FOOTER
// import SimpleFooter from "examples/Footers/SimpleFooter";

// Images
import bgImage from "assets/images/bg-basic.jpeg";
import { Link, useNavigate } from "react-router-dom";

function SignInBasic() {
  const [rememberMe, setRememberMe] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSetRememberMe = () => setRememberMe(!rememberMe);

  const Navigate = useNavigate();

  const handleStart = () => {
    if (email.trim() !== "" && password.trim() !== "") {
      if (email === "test@gmail.com" && password === "123") {

        Navigate("/start-quiz");


      } else {
        alert("Incorrect email or password");
        setEmail("");
        setPassword("");


      }
    } else {
      alert("Email and password cannot be blank");

    }
  };


  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      // If Enter key is pressed, start the quiz
      handleStart();
    }
  };

  return (
    <>
      <MKBox
        position="absolute"
        top={0}
        left={0}
        zIndex={1}
        width="100%"
        minHeight="100vh"
        sx={{
          backgroundImage: ({ functions: { linearGradient, rgba }, palette: { gradients } }) =>
            `${linearGradient(
              rgba(gradients.dark.main, 0.6),
              rgba(gradients.dark.state, 0.6)
            )}, url(${bgImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      />
      <MKBox px={1} width="100%" height="100vh" mx="auto" position="relative" zIndex={2}>
        <Grid container spacing={1} justifyContent="center" alignItems="center" height="100%">
          <Grid item xs={11} sm={9} md={5} lg={4} xl={3}>
            <Card>
              <MKBox
                variant="gradient"
                bgColor="info"
                borderRadius="lg"
                coloredShadow="info"
                mx={2}
                mt={-3}
                p={2}
                mb={1}
                textAlign="center"
              >
                <MKTypography variant="h4" fontWeight="medium" color="white" mt={1}>
                  Connecticus Quiz App
                </MKTypography>
              </MKBox>
              <MKBox pt={4} pb={3} px={3}>
                <MKBox component="form" role="form">
                  <MKBox mb={2}>
                    <MKInput type="email" label="Email" value={email} fullWidth onChange={(e) => setEmail(e.target.value)} />
                  </MKBox>
                  <MKBox mb={2}>
                    <MKInput type="password" label="Password" onKeyPress={handleKeyPress} value={password} fullWidth onChange={(e) => setPassword(e.target.value)} />
                  </MKBox>
                  <MKBox display="flex" alignItems="center" ml={-1}>
                    <Switch checked={rememberMe} onChange={handleSetRememberMe} />
                    <MKTypography
                      variant="button"
                      fontWeight="regular"
                      color="text"
                      onClick={handleSetRememberMe}
                      sx={{ cursor: "pointer", userSelect: "none", ml: -1 }}
                    >
                      &nbsp;&nbsp;Remember me
                    </MKTypography>
                  </MKBox>

                  <MKBox mt={4} mb={1}>
                    <MKButton variant="gradient" onChange={(e) => setPassword(e.target.value)}
                      onKeyPress={handleKeyPress} onClick={handleStart} color="info" fullWidth>

                      Start Quiz

                    </MKButton>
                  </MKBox>

                  {/* if we need to use same screen for login/signin */}
                  <MKBox mt={3} mb={1} textAlign="center">
                    <MKTypography variant="button" color="text">
                      Don&apos;t have an account?{" "}
                      <MKTypography
                        component={Link}
                        to="/authentication/sign-up/cover"
                        variant="button"
                        color="info"
                        fontWeight="medium"
                        textGradient
                      >
                        Sign up
                      </MKTypography>
                    </MKTypography>
                  </MKBox>

                </MKBox>
              </MKBox>
            </Card>
          </Grid>
        </Grid>
      </MKBox >
      <MKBox width="100%" position="absolute" zIndex={2} bottom="1.625rem">
        {/* <SimpleFooter light /> */}
      </MKBox>
    </>
  );
}

export default SignInBasic;
