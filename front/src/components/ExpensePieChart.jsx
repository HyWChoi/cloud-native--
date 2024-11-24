import React, { useState, useEffect } from 'react';
import { Box, Heading, Text, VStack, HStack } from '@chakra-ui/react';
import { PieChart, Pie, Cell, Tooltip } from 'recharts';

const ExpensePieChart = ({ categories = [], transactions = [] }) => {
  const [shouldRender, setShouldRender] = useState(!!localStorage.getItem('sessionId'));
  const [chartData, setChartData] = useState([]);
  const [totalExpenseAmount, setTotalExpenseAmount] = useState(0);

  useEffect(() => {
    const handleLoginState = () => {
      setShouldRender(!!localStorage.getItem('sessionId'));
    };

    window.addEventListener('login', handleLoginState);
    window.addEventListener('logout', handleLoginState);

    return () => {
      window.removeEventListener('login', handleLoginState);
      window.removeEventListener('logout', handleLoginState);
    };
  }, []);

  // 카테고리별 지출 비율 계산
  useEffect(() => {
    if (!transactions.length || !categories.length) {
      setChartData([]);
      setTotalExpenseAmount(0);
      return;
    }

    // 지출 트랜잭션만 필터링 (expense 소문자로 수정)
    const expenses = transactions.filter(t => t.transactionType === 'expense');
    
    // 총 지출 금액 계산
    const totalExpense = expenses.reduce((sum, t) => sum + t.amount, 0);
    setTotalExpenseAmount(totalExpense);
    
    // 카테고리별 지출 합계 및 비율 계산
    const categoryExpenses = categories.reduce((acc, category) => {
      const categoryTotal = expenses
        .filter(t => t.categories?.some(c => c.id === category.id))
        .reduce((sum, t) => sum + t.amount, 0);
        
      if (categoryTotal > 0) {
        acc.push({
          id: category.id,
          name: category.content,
          value: Math.round((categoryTotal / totalExpense) * 100),
          amount: categoryTotal,
          color: `hsl(${(acc.length * 360) / categories.length}, 70%, 50%)`
        });
      }
      return acc;
    }, []);

    // 금액이 큰 순서대로 정렬
    categoryExpenses.sort((a, b) => b.amount - a.amount);
    setChartData(categoryExpenses);
  }, [categories, transactions]);

  if (!shouldRender) return null;

  if (!chartData.length) {
    return (
      <Box p={4} borderWidth={1} borderColor="gray.200" borderRadius="lg">
        <VStack align="center" justify="center" height="200px">
          <Text color="gray.500">지출 데이터가 없습니다</Text>
        </VStack>
      </Box>
    );
  }

  return (
    <Box p={4} borderWidth={1} borderColor="gray.200" borderRadius="lg">
      <HStack spacing={8} align="start">
        <Box width="200px" height="200px">
          <PieChart width={200} height={200}>
            <Pie
              data={chartData}
              cx={100}
              cy={100}
              innerRadius={0}
              outerRadius={80}
              dataKey="value"
              label={({name, value}) => `${name} (${value}%)`}
            >
              {chartData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip 
              formatter={(value, name, props) => [
                `${props.payload.amount.toLocaleString()}원 (${value}%)`,
                name
              ]}
            />
          </PieChart>
        </Box>
        <Box flex="1">
          <VStack align="stretch" spacing={4}>
            <Heading size="md">카테고리별 지출 비율</Heading>
            <Text color="gray.600">
              총 지출: {totalExpenseAmount.toLocaleString()}원
            </Text>
            <VStack align="stretch" spacing={2}>
              {chartData.map((category, index) => (
                <HStack key={`legend-${index}`} justify="space-between">
                  <HStack>
                    <Box 
                      w="3" 
                      h="3" 
                      borderRadius="sm"
                      bg={category.color}
                    />
                    <Text>{category.name}</Text>
                  </HStack>
                  <VStack spacing={0} align="flex-end">
                    <Text fontWeight="medium">{category.amount.toLocaleString()}원</Text>
                    <Text fontSize="sm" color="gray.600">{category.value}%</Text>
                  </VStack>
                </HStack>
              ))}
            </VStack>
          </VStack>
        </Box>
      </HStack>
    </Box>
  );
};

export default ExpensePieChart;