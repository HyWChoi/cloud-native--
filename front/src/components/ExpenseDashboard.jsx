import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  GridItem,
  VStack,
  useToast,
} from '@chakra-ui/react';
import DashboardHeader from './DashboardHeader';
import ExpensePieChart from './ExpensePieChart';
import LoginBox from './LoginBox';
import TransactionTable from './TransactionTable';
import CategoryModal from './CategoryModal';
import TransactionModal from './TransactionModal';

const ExpenseDashboard = () => {
  const [transactions, setTransactions] = useState([]);
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const toast = useToast();

  // 카테고리 데이터 불러오기
  const fetchCategories = async () => {
    try {
      const sessionId = localStorage.getItem('sessionId');
      if (!sessionId) {
        setCategories([]);
        return;
      }

      const response = await fetch('http://localhost:8080/api/v1/categories/get', {
        headers: {
          'X-Session-ID': sessionId
        }
      });

      if (!response.ok) {
        throw new Error('카테고리 목록을 불러오는데 실패했습니다.');
      }

      const data = await response.json();
      console.log('카테고리 데이터:', data);
      setCategories(data.categories || []);
    } catch (error) {
      console.error('카테고리 로드 에러:', error);
      toast({
        title: '카테고리 로드 실패',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  // 트랜잭션 데이터 불러오기
  const fetchTransactions = async () => {
    try {
      setIsLoading(true);
      const sessionId = localStorage.getItem('sessionId');
      if (!sessionId) {
        setTransactions([]);
        return;
      }

      const response = await fetch(
        'http://localhost:8080/api/v1/transactions/profile',
        {
          headers: {
            'X-Session-ID': sessionId
          }
        }
      );

      if (!response.ok) {
        throw new Error('거래 내역을 불러오는데 실패했습니다.');
      }

      const data = await response.json();
      console.log('트랜잭션 데이터:', data);
      setTransactions(data.transactions || []);
    } catch (error) {
      console.error('트랜잭션 로드 에러:', error);
      toast({
        title: '데이터 로드 실패',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  // 데이터 새로고침
  const refreshData = async () => {
    const sessionId = localStorage.getItem('sessionId');
    if (sessionId) {
      await Promise.all([fetchCategories(), fetchTransactions()]);
    } else {
      setCategories([]);
      setTransactions([]);
    }
  };

  // 로그인 상태 변경 감지 및 데이터 로드
  useEffect(() => {
    const checkLoginAndLoadData = () => {
      const sessionId = localStorage.getItem('sessionId');
      console.log('Session ID 체크:', sessionId);
      setIsLoggedIn(!!sessionId);
      refreshData();
    };

    // 초기 로딩
    checkLoginAndLoadData();

    // 로그인/로그아웃 이벤트 리스너
    const handleLoginEvent = () => {
      console.log('Login/Logout event detected');
      checkLoginAndLoadData();
    };

    window.addEventListener('login', handleLoginEvent);
    window.addEventListener('logout', handleLoginEvent);

    return () => {
      window.removeEventListener('login', handleLoginEvent);
      window.removeEventListener('logout', handleLoginEvent);
    };
  }, []);

  const handleLoginSuccess = () => {
    console.log('Login success detected');
    const sessionId = localStorage.getItem('sessionId');
    setIsLoggedIn(!!sessionId);
    refreshData();
  };

  const handleDataUpdate = () => {
    console.log('Data update requested');
    refreshData();
  };

  return (
    <Container maxW="container.xl" p={4}>
      <VStack spacing={6} align="stretch">
        <DashboardHeader />

        <Grid templateColumns="2fr 1fr" gap={4}>
          <GridItem>
            <ExpensePieChart 
              categories={categories} 
              transactions={transactions}
              isLoggedIn={isLoggedIn}
            />
          </GridItem>
          <GridItem>
            <LoginBox 
              onLoginSuccess={handleLoginSuccess}
            />
          </GridItem>
        </Grid>

        {isLoggedIn && (
          <Grid templateColumns="1fr 1fr" gap={4}>
            <GridItem>
              <CategoryModal onSuccess={handleDataUpdate} />
            </GridItem>
            <GridItem>
              <TransactionModal 
                categories={categories}
                onSuccess={handleDataUpdate}
              />
            </GridItem>
          </Grid>
        )}

        {isLoggedIn && (
          <TransactionTable 
            transactions={transactions}
            onTransactionUpdate={handleDataUpdate}
            isLoading={isLoading}
          />
        )}
      </VStack>
    </Container>
  );
};

export default ExpenseDashboard;