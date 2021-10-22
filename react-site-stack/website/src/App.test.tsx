import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders heading', () => {
  render(<App />);
  const heading = screen.getByText(
    /Nitric example with Create React App TypeScript and Material UI/i
  );
  expect(heading).toBeInTheDocument();
});
