/**
 * @author: yuehan124@gmail.com
 * @since: 2025/09/22
 **/
import { FC, memo } from 'react';
import { Layout } from 'antd';
import { Outlet } from 'react-router-dom';
import Header from './header/index';

import './index.less';

interface IProps {}

const L: FC<IProps> = () => {

  return (
    <Layout className="layout">
      <Header />
      <div style={{ flex: 1 }}>
        <Outlet />
      </div>
    </Layout>
  );
};

const AppLayout = memo(L);

export default AppLayout;
