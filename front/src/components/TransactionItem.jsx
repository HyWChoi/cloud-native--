import React from 'react';
import { 
  Tr, 
  Td, 
  Button, 
  Tag, 
  HStack, 
  Text, 
  useToast 
} from '@chakra-ui/react';
import TransactionModal from './TransactionModal';

const TransactionItem = ({ transaction, onSuccess }) => {
  const toast = useToast();
  const sessionId = localStorage.getItem('sessionId');

  // sessionId가 없으면 아이템을 렌더링하지 않음
  if (!sessionId) return null;

  const handleDelete = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/v1/transactions/${transaction.id}`,
        {
          method: 'DELETE',
          headers: {
            'X-Session-ID': sessionId
          }
        }
      );

      if (!response.ok) {
        throw new Error('거래 내역 삭제에 실패했습니다.');
      }

      toast({
        title: '거래 내역이 삭제되었습니다.',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });

      if (onSuccess) {
        onSuccess();
      }
    } catch (error) {
      console.error('삭제 중 에러:', error);
      toast({
        title: '오류가 발생했습니다',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    }).format(date);
  };

  return (
    <Tr>
      <Td>
        <Tag
          colorScheme={transaction.transactionType === 'income' ? 'green' : 'red'}
          size="md"
        >
          {transaction.transactionType === 'income' ? '입금' : '출금'}
        </Tag>
      </Td>
      <Td color={transaction.transactionType === 'income' ? 'green.500' : 'red.500'} fontWeight="bold">
        {transaction.amount.toLocaleString()}원
      </Td>
      <Td maxW="300px">
        <Text noOfLines={2}>{transaction.description}</Text>
      </Td>
      <Td>{formatDate(transaction.date)}</Td>
      <Td>
        <HStack spacing={2}>
          {transaction.categories?.map((category) => (
            <Tag key={category.id} size="sm" colorScheme="blue">
              {category.content}
            </Tag>
          ))}
        </HStack>
      </Td>
      <Td>
        <TransactionModal 
          isEdit={true}
          transaction={transaction}
          onSuccess={onSuccess}
        />
      </Td>
      <Td>
        <Button 
          size="sm" 
          colorScheme="red"
          onClick={handleDelete}
        >
          삭제
        </Button>
      </Td>
    </Tr>
  );
};

export default TransactionItem;