/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-22
 **/
import { Button, Descriptions, Form, Input, Tabs, message } from 'antd';
import TabPane from 'antd/es/tabs/TabPane';
import {
  FC,
  memo,
  useEffect,
  useState,
} from 'react';
import { getUserById } from '../../api/user';
import useRequest from '../../hooks/useRequest';
import { t } from 'i18next';

interface IProps {
}

interface UserType {
  id: string;
  name: string;
  email: string;
  cellphone: string;
  passwd: string;
  createTime: string
}

const S: FC<IProps> = () => {
  const [form] = Form.useForm();
  const [activeKey, setActiveKey] = useState('info');
  const [user, setUser] = useState<UserType>();

  useEffect(() => {
    infoRequest.caller();
    document.title = t('account.title');
  }, []);

  const infoRequest = useRequest(getUserById, {
    wrapperFun: (data: any) => {
      const user: UserType = {
        id: data.id,
        name: data.name,
        email: data.email,
        cellphone: data.cellphone,
        passwd: data.passwd,
        createTime: data.createTime,
      };
      setUser(user);
      return user;
    },
  });

  const onChange = (key: string) => {
    setActiveKey(key);
  };

  const onReset = () => {
    form.resetFields();
  };

  const onSubmit = () => {
    message.warning(t('account.warning'));
  };


  return (
    <div className="module">
      <Tabs activeKey={activeKey} onChange={onChange}>
        <TabPane tab={t('account.info')} key="info">
          <Descriptions title="">
            <Descriptions.Item span={3} label={t('account.name')}>{user?.name}</Descriptions.Item>
            <Descriptions.Item span={3} label={t('account.cellphone')}>{user?.cellphone}</Descriptions.Item>
            <Descriptions.Item span={3} label={t('account.email')}>{user?.email}</Descriptions.Item>
            <Descriptions.Item span={3} label={t('account.createTime')}>{user?.createTime}</Descriptions.Item>
          </Descriptions>
        </TabPane>

        <TabPane tab={t('account.edit')} key="edit">
          <Form layout="horizontal" form={form} style={{ marginBottom: '0px' }}
            labelCol={{ span: 3 }}
            wrapperCol={{ span: 5 }}>
            <Form.Item name="user" label={t('account.name')} initialValue='testuser'>
              <Input style={{ border: 0 }} />
            </Form.Item>
            <Form.Item name="oldPwd" label={t('account.oldPwd')} rules={[{ required: true, message: '' }]}>
              <Input.Password autoComplete="off" />
            </Form.Item>
            <Form.Item name="newPwd" label={t('account.newPwd')} rules={[{ required: true, message: '' }]}>
              <Input.Password autoComplete="new-password" />
            </Form.Item>
            <Form.Item name="newPwd2" label={t('account.newPwd2')} rules={[{ required: true, message: '' }]}>
              <Input.Password autoComplete="new-password" />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 3, span: 6 }}>
              <Button htmlType="button" onClick={onReset} style={{ marginRight: 20 }}>{t('account.reset')}</Button>
              <Button type="primary" htmlType="submit" onClick={onSubmit}>{t('account.submit')}</Button>
            </Form.Item>
          </Form>
        </TabPane>
      </Tabs>
    </div>
  );
};

const UserInfo = memo(S);

export default UserInfo;
