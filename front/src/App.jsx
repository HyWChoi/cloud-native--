import { useState } from 'react'
import ExpenseDashboard from './components/ExpenseDashboard';
import { ChakraProvider } from '@chakra-ui/react';

function App() {

  return (
    <ChakraProvider >
      <ExpenseDashboard />
    </ChakraProvider>
  )
}

export default App
