import React from 'react';
import { Box, Heading } from '@chakra-ui/react';

const DashboardHeader = () => {
  return (
    <Box p={4} borderWidth={1} borderColor="gray.200" borderRadius="lg">
      <Heading size="lg">지출 관리 대시보드</Heading>
    </Box>
  );
};

export default DashboardHeader;
