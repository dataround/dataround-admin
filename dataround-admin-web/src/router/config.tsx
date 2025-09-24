/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-22
 **/
import { ReactNode } from "react";
import {
  FolderOutlined,
  SettingOutlined,
  TeamOutlined
} from "@ant-design/icons";
import AppLayout from "../layout/index";
import { lazyReactElement } from "../utils/index";
import { useTranslation } from "react-i18next";
import { AuthRouter } from "./authRouter";
import LoginLayout from "../layout/login";

export interface IMenu {
  name?: string;
  path?: string;
  children?: IMenu[];
  icon?: ReactNode;
  element?: ReactNode | null;
  hidden?: boolean;
}

const fallback = <div>loading</div>;

export const useRoutes = () => {
  const { t } = useTranslation();

  const routes: IMenu[] = [
    {
      element: (
        <AuthRouter>
          <AppLayout />
        </AuthRouter>
      ),
      children: [
        {
          name: "home",
          path: "/",
          element: lazyReactElement(() => import("../pages/home"), fallback),
          children: [
            {
              path: "/project",
              name: t('menu.projectManagement'),
              icon: <FolderOutlined />,
              element: lazyReactElement(() => import("../pages/project"), fallback),
            },
            {
              path: "/user",
              name: t('menu.userManagement'),
              icon: <TeamOutlined />,
              element: lazyReactElement(() => import("../pages/user/"), fallback),
            },
            {
              path: "/account",
              name: t('menu.myAccount'),
              icon: <SettingOutlined />,
              element: lazyReactElement(
                () => import("../pages/account"),
                fallback
              ),
            }
          ]
        }
      ]
    },
    {
      element: <LoginLayout />,
      path: "/login",
    }
  ];

  return routes;
};
