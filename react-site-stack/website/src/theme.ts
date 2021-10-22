import { createTheme } from '@mui/material/styles';
import { red } from '@mui/material/colors';

// A custom theme for this app
const theme = createTheme({
  palette: {
    primary: {
      main: '#FF9B01',
    },
    secondary: {
      main: '#5C629E',
    },
    error: {
      main: red.A400,
    },
  },
});

export default theme;
