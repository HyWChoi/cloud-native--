import React, { useEffect, useState } from 'react';
import {
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  FormControl,
  FormLabel,
  Input,
  Select,
  VStack,
  useDisclosure,
  useToast
} from '@chakra-ui/react';

const TransactionModal = ({ 
  isEdit = false, 
  transaction = null,
  onSuccess 
}) => {
  const { isOpen, onOpen, onClose } = useDisclosure({ defaultIsOpen: false });
  const initialRef = React.useRef();
  const toast = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const [categories, setCategories] = useState([]);

  const [formData, setFormData] = useState({
    type: 'expense',
    categoryIds: [],
    description: '',
    amount: '',
    date: new Date().toISOString().split('T')[0],
  });

  // 카테고리 데이터 가져오기
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const sessionId = localStorage.getItem('sessionId');
        if (!sessionId) return;

        const response = await fetch('http://localhost:8080/api/v1/categories/get', {
          headers: {
            'X-Session-ID': sessionId
          }
        });

        if (!response.ok) {
          throw new Error('카테고리 목록을 불러오는데 실패했습니다.');
        }

        const data = await response.json();
        setCategories(data.categories || []);
      } catch (error) {
        toast({
          title: '카테고리 로드 실패',
          description: error.message,
          status: 'error',
          duration: 3000,
          isClosable: true,
        });
      }
    };

    if (isOpen) {
      fetchCategories();
    }
  }, [isOpen, toast]);

  // 수정 모드일 때 데이터 설정
  useEffect(() => {
    if (isEdit && transaction && isOpen) {
      setFormData({
        type: transaction.type || 'expense',
        categoryIds: transaction.categories?.map(cat => cat.id) || [],
        description: transaction.description || '',
        amount: transaction.amount?.toString() || '',
        date: transaction.date || new Date().toISOString().split('T')[0],
      });
    }
  }, [isEdit, transaction, isOpen]);

  // 모달이 닫힐 때 폼 데이터 초기화
  const handleClose = () => {
    setFormData({
      type: 'expense',
      categoryIds: [],
      description: '',
      amount: '',
      date: new Date().toISOString().split('T')[0],
    });
    onClose();
  };

  const handleSubmit = async () => {
    try {
      setIsLoading(true);
      const sessionId = localStorage.getItem('sessionId');
      
      if (!sessionId) {
        toast({
          title: '로그인이 필요합니다',
          status: 'error',
          duration: 3000,
          isClosable: true,
        });
        return;
      }

      if (!formData.categoryIds.length) {
        toast({
          title: '카테고리를 선택해주세요',
          status: 'error',
          duration: 3000,
          isClosable: true,
        });
        return;
      }

      const payload = {
        ...formData,
        amount: Number(formData.amount),
        date: new Date(formData.date).toISOString(),
      };

      const url = isEdit 
        ? `http://localhost:8080/api/v1/transactions/edit/${transaction.id}`
        : 'http://localhost:8080/api/v1/transactions/create';

      const response = await fetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-Session-ID': sessionId
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || '거래 내역 저장에 실패했습니다.');
      }

      toast({
        title: isEdit ? '거래 내역이 수정되었습니다.' : '거래 내역이 추가되었습니다.',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });

      handleClose();
      if (onSuccess) {
        onSuccess();
      }
    } catch (error) {
      toast({
        title: '오류가 발생했습니다',
        description: error.message,
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'category') {
      setFormData(prev => ({
        ...prev,
        categoryIds: [Number(value)] // 단일 카테고리 선택의 경우
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  return (
    <>
      {isEdit ? (
        <Button size="sm" onClick={onOpen}>수정</Button>
      ) : (
        <Button width="100%" onClick={onOpen}>
          [ 입금 / 출금 ] 기록 추가
        </Button>
      )}

      <Modal
        initialFocusRef={initialRef}
        isOpen={isOpen}
        onClose={handleClose}
        size="lg"
      >
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>
            {isEdit ? '거래 내역 수정' : '[ 입금 / 출금 ] 추가'}
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
            <VStack spacing={4}>
              <FormControl isRequired>
                <FormLabel>거래 타입</FormLabel>
                <Select
                  name="type"
                  value={formData.type}
                  onChange={handleChange}
                >
                  <option value="income">입금</option>
                  <option value="expense">출금</option>
                </Select>
              </FormControl>

              <FormControl isRequired>
                <FormLabel>카테고리</FormLabel>
                <Select
                  name="category"
                  value={formData.categoryIds[0] || ''}
                  onChange={handleChange}
                >
                  <option value="">카테고리를 선택하세요</option>
                  {categories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.content}
                    </option>
                  ))}
                </Select>
              </FormControl>

              <FormControl isRequired>
                <FormLabel>내용</FormLabel>
                <Input
                  name="description"
                  ref={initialRef}
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="예: 맛있는 짜장면, 친구와 만남 등"
                />
              </FormControl>

              <FormControl isRequired>
                <FormLabel>금액</FormLabel>
                <Input
                  name="amount"
                  type="number"
                  value={formData.amount}
                  onChange={handleChange}
                  placeholder="금액을 입력하세요"
                  min="0"
                />
              </FormControl>

              <FormControl isRequired>
                <FormLabel>날짜</FormLabel>
                <Input
                  name="date"
                  type="date"
                  value={formData.date}
                  onChange={handleChange}
                />
              </FormControl>
            </VStack>
          </ModalBody>

          <ModalFooter>
            <Button 
              colorScheme="blue" 
              mr={3} 
              onClick={handleSubmit}
              isLoading={isLoading}
              loadingText="저장 중..."
            >
              {isEdit ? '수정' : '저장'}
            </Button>
            <Button onClick={handleClose}>취소</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
};

export default TransactionModal;