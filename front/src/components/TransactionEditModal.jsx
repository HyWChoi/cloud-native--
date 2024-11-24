import React from 'react';
import { Tr, Td, Button } from '@chakra-ui/react';
import TransactionModal from './TransactionModal';

const TransactionItem = ({ transaction, categories }) => {
  return (
    <Tr>
      <Td>{transaction.paymentMethod}</Td>
      <Td>{transaction.amount.toLocaleString()}원</Td>
      <Td>{transaction.description}</Td>
      <Td>{transaction.date}</Td>
      <Td>{transaction.category}</Td>
      <Td>
        <TransactionModal 
          isEdit={true}
          transaction={transaction}
          categories={categories}
          triggerButton={
            <Button size="sm">수정</Button>
          }
        />
      </Td>
      <Td>
        <Button size="sm" colorScheme="red">제거</Button>
      </Td>
    </Tr>
  );
};

export default TransactionItem;