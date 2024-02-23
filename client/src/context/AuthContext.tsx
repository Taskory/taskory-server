import React, {createContext, ReactNode, useContext, useState} from 'react';

// 사용자 인증 정보 타입 정의
interface AuthContextType {
  token: string | null;
  login: (token: string) => void;
  logout: () => void;
}

// 인증 컨텍스트 생성
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// AuthProvider 컴포넌트 생성
export const AuthProvider: React.FC<{children: ReactNode}> = ({children}) => {
  const [token, setToken] = useState<string | null>(null);

  // 로그인 함수
  const login = (token: string) => {
    setToken(token);
  };

  // 로그아웃 함수
  const logout = () => {
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// 사용자 정의 훅 생성
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
