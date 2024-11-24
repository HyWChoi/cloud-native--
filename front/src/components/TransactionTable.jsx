import React, { useState, useEffect } from 'react';
import {
  Box,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  HStack,
  Select,
  Input,
  useToast,
  Text,
  Flex,
  Spinner
} from '@chakra-ui/react';
import TransactionItem from './TransactionItem';

const TransactionTable = ({ transactions = [], onTransactionUpdate, isLoading }) => {
  const [filteredTransactions, setFilteredTransactions] = useState(transactions);
  const [filters, setFilters] = useState({
    type: '',
    startDate: '',
    endDate: ''
  });
  const sessionId = localStorage.getItem('sessionId');

  // sessionId가 없으면 테이블을 렌더링하지 않음
  if (!sessionId) return null;

  useEffect(() => {
    applyFilters(transactions);
  }, [transactions]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    const newFilters = { ...filters, [name]: value };
    setFilters(newFilters);
    applyFilters(transactions, newFilters);
  };

  const applyFilters = (transactionList, currentFilters = filters) => {
    let filtered = [...transactionList];

    if (currentFilters.type) {
      filtered = filtered.filter(t => t.transactionType === currentFilters.type);
    }

    if (currentFilters.startDate) {
      filtered = filtered.filter(t => new Date(t.date) >= new Date(currentFilters.startDate));
    }

    if (currentFilters.endDate) {
      filtered = filtered.filter(t => new Date(t.date) <= new Date(currentFilters.endDate));
    }

    setFilteredTransactions(filtered);
  };

  const calculateTotal = (type) => {
    return filteredTransactions
      .filter(t => t.transactionType === type)
      .reduce((sum, t) => sum + t.amount, 0);
  };

  if (isLoading) {
    return (
      <Flex justify="center" align="center" h="200px">
        <Spinner size="xl" />
      </Flex>
    );
  }

  return (
    <Flex direction="column" gap={4} width="100%">
      <HStack spacing={4} width="100%">
        <Select
          name="type"
          placeholder="전체 유형"
          value={filters.type}
          onChange={handleFilterChange}
        >
          <option value="income">입금</option>
          <option value="expense">출금</option>
        </Select>
        <Input
          type="date"
          name="startDate"
          value={filters.startDate}
          onChange={handleFilterChange}
        />
        <Input
          type="date"
          name="endDate"
          value={filters.endDate}
          onChange={handleFilterChange}
        />
      </HStack>

      <Flex justify="space-between" width="100%" align="center">
        <Text>총 {filteredTransactions.length}개의 거래내역</Text>
        <HStack spacing={8}>
          <Text fontSize="lg">
            총 입금: <span style={{ color: 'green' }}>{calculateTotal('income').toLocaleString()}원</span>
          </Text>
          <Text fontSize="lg">
            총 출금: <span style={{ color: 'red' }}>{calculateTotal('expense').toLocaleString()}원</span>
          </Text>
          <Text fontSize="lg" fontWeight="bold">
            잔액: {(calculateTotal('income') - calculateTotal('expense')).toLocaleString()}원
          </Text>
        </HStack>
      </Flex>

      <Box borderWidth={1} borderColor="gray.200" borderRadius="lg" overflow="hidden">
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>거래 유형</Th>
              <Th>금액</Th>
              <Th>설명</Th>
              <Th>날짜</Th>
              <Th>카테고리</Th>
              <Th>수정</Th>
              <Th>삭제</Th>
            </Tr>
          </Thead>
          <Tbody>
            {filteredTransactions.map((transaction) => (
              <TransactionItem
                key={transaction.id}
                transaction={transaction}
                onSuccess={onTransactionUpdate}
              />
            ))}
          </Tbody>
        </Table>
      </Box>
    </Flex>
  );
};

export default TransactionTable;