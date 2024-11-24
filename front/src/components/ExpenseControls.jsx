import React from 'react';
import { Grid, GridItem, Box, Button } from '@chakra-ui/react';
import DropDownSelect from './DropDownSelect';

const ExpenseControls = () => {
  return (
    <Grid templateColumns="1fr auto auto auto" gap={4}>
      <GridItem>
        <Box p={4} borderWidth={1} borderColor="gray.200" borderRadius="lg">
          <DropDownSelect title="수입 / 지출"></DropDownSelect>
        </Box>
      </GridItem>
      <GridItem>
        <Button>추가</Button>
      </GridItem>
      <GridItem>
        <Button>제거</Button>
      </GridItem>
      <GridItem>
        <Button>수정</Button>
      </GridItem>
    </Grid>
  );
};

export default ExpenseControls;