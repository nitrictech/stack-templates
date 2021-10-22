import * as React from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import Stack from '@mui/material/Stack';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import TextField from '@mui/material/TextField';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import FormHelperText from '@mui/material/FormHelperText';

export interface Example {
  name: string;
  description: string;
}

function Copyright() {
  return (
    <Typography variant='body2' color='text.secondary' align='center'>
      {'Copyright © '}
      <Link color='inherit' href='https://nitric.io/'>
        Your Website
      </Link>{' '}
      {new Date().getFullYear()}.
    </Typography>
  );
}

const addExample = async (example: Example) => {
  await fetch('/examples', {
    method: 'POST',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    credentials: 'same-origin',
    body: JSON.stringify(example),
  });
};

function Examples() {
  const [name, setName] = React.useState('');
  const [description, setDescription] = React.useState('');
  const [error, setError] = React.useState(false);
  const [examples, setExamples] = React.useState<Example[]>([]);

  const fetchExamples = async () => {
    const data = await fetch('/examples').then((res) => res.json());

    setExamples(data);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (name === '' || description === '') {
      setError(true);
      return null;
    }

    const newExample = {
      name,
      description,
    };

    await addExample(newExample);

    await fetchExamples();
  };

  React.useEffect(() => {
    fetchExamples();
  }, []);

  return (
    <Stack spacing={2} component='form' onSubmit={handleSubmit}>
      <TextField
        label='New Example Name'
        color='secondary'
        variant='outlined'
        required
        onChange={(event) => setName(event.target.value)}
        value={name}
      />
      <TextField
        label='New Example Description'
        multiline
        color='secondary'
        variant='outlined'
        required
        onChange={(event) => setDescription(event.target.value)}
        value={description}
      />
      {error && (
        <FormHelperText error>Error, all fields are required!</FormHelperText>
      )}
      <Button color='secondary' variant='contained' type='submit'>
        Add Example
      </Button>
      <Paper>
        <List>
          {examples.map((examples, i) => (
            <ListItemButton key={i}>
              <ListItemText
                primary={`${i + 1}. ${examples.name} - ${examples.description}`}
              />
            </ListItemButton>
          ))}
        </List>
      </Paper>
    </Stack>
  );
}

export default function App() {
  return (
    <Container maxWidth='sm'>
      <Stack sx={{ my: 4 }} spacing={4}>
        <Box
          sx={{
            textAlign: 'center',
          }}
        >
          <img src={'https://nitric.io/img/nitric-logo.svg'} alt='Logo' />
        </Box>

        <Typography
          color='secondary.dark'
          variant='h4'
          component='h1'
          gutterBottom
        >
          Nitric example with Create React App TypeScript and Material UI
        </Typography>
        <Examples />
        <Copyright />
      </Stack>
    </Container>
  );
}
