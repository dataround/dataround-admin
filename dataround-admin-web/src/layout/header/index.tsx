/**
 * @author: yuehan124@gmail.com 
 * @since: 2025-09-22
 **/
import { CheckOutlined, FolderOutlined, PoweroffOutlined, ProjectOutlined, QuestionCircleOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons';
import { Avatar, Col, Dropdown, Layout, Menu, MenuProps, Row, Space } from 'antd';
import { SubMenuType } from 'antd/es/menu/interface';
import { FC, memo, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { doLogout } from '../../api/login';
import { getMyProjects, updateSelected } from '../../api/user';
import useRequest from '../../hooks/useRequest';
import './index.less';
import logoImage from '../../assets/logo.v20251009.png';
import { useTranslation } from 'react-i18next';
import LanguageSwitcher from '../../components/LanguageSwitcher';

// eslint-disable-next-line @typescript-eslint/no-empty-interface
interface IProps { }

const { Header: AntHeader } = Layout;

type MenuItem = Required<MenuProps>['items'][number];

const H: FC<IProps> = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { t } = useTranslation();
  let selected = '/batch/job';
  const [current, setCurrent] = useState(selected);

  const items: MenuItem[] = [
    {
      label: t('menu.dataAdmin'),
      key: '/'
    },
    {
      label: t('menu.doc'),
      key: 'doc'
    }
  ];

  const onClick: MenuProps['onClick'] = (e) => {
    setCurrent(e.key);
    console.log(e.key);
    if (e.key === 'doc')
      window.open('https://dataround.io/doc', '_blank');
  };
  
  useEffect(() => {
    projectReq.caller();
  }, [])

  const logoutReq = useRequest(doLogout, {
    wrapperFun: (resp: any) => {
      sessionStorage.removeItem('info');
      navigate('/login', { replace: true })
    }
  });

  const logout = () => {
    logoutReq.caller();
  }

  const userItemOptions: MenuProps['items'] = [
    {
      key: '1',
      label: '',
      children: [],
      onClick: (e: any) => {
        updateSelected(e.key).then((resp: any) => {
          window.location.reload();
        })
      }
    },
    {
      key: 'divider1',
      type: 'divider'
    },
    {
      key: '2',
      label: <><PoweroffOutlined style={{ color: '#ff4d4f' }} />  {t('menu.logout')}</>,
      onClick: () => logout()
    },
  ]

  const [userItems, setUserItems] = useState(userItemOptions);
  const projectReq = useRequest(getMyProjects, {
    wrapperFun: (data: any) => {
      const projects = new Array();
      let selectedProject = '';
      data.forEach((item: any) => {
        if (item.selected) {
          selectedProject = item.name;
        };
        projects.push({
          key: item.id,
          label: <>{item.selected ? <CheckOutlined style={{ marginLeft: 8, color: '#1890ff' }} /> : <span style={{ marginLeft: 8, width: 16, display: 'inline-block' }}></span>} &nbsp;&nbsp;{item.name}&nbsp;&nbsp;&nbsp;&nbsp; </>
        });
      })
      const item0 = userItemOptions[0] as SubMenuType;
      item0.label = <><FolderOutlined style={{ color: '#1890ff' }} />  {t('menu.project')}: {selectedProject}</>;
      item0.children = projects;
      setUserItems(userItemOptions);
    }
  });

  return (
    <AntHeader className="layout-header" style={{ padding: 0 }}>
      <Row>
        <Col span={4}>
          <a href='/'><img src={logoImage}></img></a>
        </Col>
        <Col span={12}>
          <Menu style={{ backgroundColor: 'transparent' }} onClick={onClick} selectedKeys={[current]} items={items} mode="horizontal">
          </Menu>
        </Col>
        <Col span={8} style={{ textAlign: 'right', paddingRight: '20px' }}>
          <div style={{float: 'right', marginRight: 35}}>
            <Dropdown menu={{ items: userItems }} overlayStyle={{ width: 210 }} placement="bottomRight" trigger={['hover']}>
              <span style={{ cursor: 'pointer', display: 'inline-block' }}>
                <Space align="center">
                  <UserOutlined />
                  <label>{t(`menu.user`)} </label>
                </Space>
              </span>
            </Dropdown>
          </div>       
          <LanguageSwitcher />    
        </Col>
      </Row>
    </AntHeader>     
  );
};

const Header = memo(H);

export default Header;
