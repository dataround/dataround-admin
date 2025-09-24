/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-22
 **/
import { Navigate } from "react-router-dom";
import { ReactNode } from "react";

const isExpired = (expireTime: number) => {
  const currentTime = Date.now();
  return currentTime > expireTime;
};

const getUserInfo = () => {
  const info = sessionStorage.getItem("info");
  if (info) {
    try {
      const tokenInfo = JSON.parse(info);
      if (!isExpired(tokenInfo.expire)) {
        return tokenInfo.name;
      }
    } catch (e) {
      console.error("parse token error", e);
    }
  }
  return null;
};

function AuthRouter({ children }: { children: ReactNode }) {
  if (getUserInfo()) {
    return <>{children}</>;
  } else {
    return <Navigate to={"/login"} replace={true}></Navigate>;
  }
}

export { AuthRouter };
