/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-21
 **/
import { lazy, Suspense, ReactNode } from "react";
import axios from './ajax';

export const baseAPI: string = '/dataadmin/api';

export const lazyReactElement = (
  loader: () => Promise<any>,
  fallback?: ReactNode,
  appName?: String
) => {
  const Component = lazy(loader);
  return (
    <Suspense fallback={fallback}>
      <Component appName={appName} />
    </Suspense>
  );
};

export const http = axios;
