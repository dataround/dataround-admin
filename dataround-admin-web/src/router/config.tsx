/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-22
 **/
import { ReactNode } from "react";
import {
  FolderOutlined,
  LockOutlined,
  SettingOutlined,
  TeamOutlined,
  UserOutlined
} from "@ant-design/icons";
import AppLayout from "../layout/index";
import { lazyReactElement } from "../utils/index";
import { useTranslation } from "react-i18next";
import { AuthRouter } from "./authRouter";
import LoginLayout from "../layout/login";
import { hasPermission, useStore } from "../store";

export interface IMenu {
  name?: string;
  path?: string;
  children?: IMenu[];
  icon?: ReactNode;
  element?: ReactNode | null;
  hidden?: boolean;
  permissionKey?: string;  // Resource key for permission check
}

const fallback = <div>loading</div>;

export const useRoutes = () => {
  const { t } = useTranslation();
  useStore();  // Subscribe to store changes for re-render

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
              permissionKey: 'menu:project',
            },
            {
              path: "/projectMember",
              name: t('menu.projectMember'),
              icon: <UserOutlined />,
              element: lazyReactElement(() => import("../pages/projectMember"), fallback),
              permissionKey: 'menu:projectMember',
            },
            {
              path: "/user",
              name: t('menu.userManagement'),
              icon: <TeamOutlined />,
              element: lazyReactElement(() => import("../pages/user/"), fallback),
              permissionKey: 'menu:user',
            },
            {
              path: "/myInfo",
              name: t('menu.myAccount'),
              icon: <SettingOutlined />,
              element: lazyReactElement(
                () => import("../pages/myInfo"),
                fallback
              ),
              // No permission key - always visible
            },
            {
              path: "/permission",
              name: t('menu.permission'),
              icon: <LockOutlined />,
              element: lazyReactElement(() => import("../pages/permission"), fallback),
              permissionKey: 'menu:permission',
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

  // Filter routes by permission
  return filterMenusByPermission(routes);
};

/**
 * Filter menus by user permission
 */
const filterMenusByPermission = (menus: IMenu[]): IMenu[] => {
  return menus
    .filter(menu => {
      // No permission key means always visible
      if (!menu.permissionKey) {
        return true;
      }
      return hasPermission(menu.permissionKey);
    })
    .map(menu => {
      if (menu.children) {
        const filteredChildren = filterMenusByPermission(menu.children);
        // Hide parent menu if all children are filtered out
        if (filteredChildren.length === 0 && menu.permissionKey) {
          return { ...menu, hidden: true };
        }
        return { ...menu, children: filteredChildren };
      }
      return menu;
    });
};
