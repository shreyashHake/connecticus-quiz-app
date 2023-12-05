// @mui material components
import Icon from "@mui/material/Icon";

// Pages
import QuizScreen from "layouts/quiz-screen";
import SignIn from "layouts/sign-in";

const routes = [
  {
    name: "pages",
    icon: <Icon>dashboard</Icon>,
    columns: 1,
    rowsPerColumn: 2,
    collapse: [
      {
        name: "Quiz",
        collapse: [
          {
            name: "quiz screen",
            route: "/pages/quiz-screen",
            component: <QuizScreen />,
          },
        ],
      },
      {
        name: "account",
        collapse: [
          {
            name: "sign in",
            route: "/pages/sign-in",
            component: <SignIn />,
          },
        ],
      },
    ],
  },
];

export default routes;
