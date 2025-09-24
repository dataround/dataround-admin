/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-21
 **/
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { useRoutes } from './config';

const Router = () => {
  const routes = useRoutes();
  const router = createBrowserRouter(
    routes,
    {
      basename: '/dataadmin',
    }
  );

  return <RouterProvider router={router} />;
};

export default Router;
