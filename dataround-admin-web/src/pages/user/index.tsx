/**
 * @author: yuehan124@gmail.com
 * @since: 2025-09-22
 **/
import { EditOutlined, StopOutlined, UserAddOutlined } from "@ant-design/icons";
import {
  Button,
  Card, Col, Form,
  Input,
  Modal,
  Popconfirm, Row, Space,
  Spin,
  Table,
  TableProps,
  Tabs,
  TabsProps,
  message
} from "antd";
import { t } from "i18next";
import { FC, memo, useEffect, useState } from "react";
import { getUserList, saveOrUpdateUser } from "../../api/user";
import useRequest from "../../hooks/useRequest";

interface IProps {}

interface DataType {
  key: string;
  name: string;
  email: string;
  cellphone: string;
  passwd: string;
  createTime: string;
  updateTime: string;
  disabled: boolean;
}

const S: FC<IProps> = () => {
  const [form] = Form.useForm();
  const [refresh, setRefresh] = useState<number>(0);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalTitle, setModalTitle] = useState<string>(t('user.newUser'));
  const [tabData, setTabData] = useState<DataType[]>([]);
  const [totalCount, setTotalCount] = useState<number>(0);
  const [pageSize, setPageSize] = useState<number>(10);  


  const columns: TableProps<DataType>["columns"] = [
    {
      title: t('user.id'),
      dataIndex: "key",
      key: "key",
    },{
      title: t('user.name'),
      dataIndex: "name",
      key: "name",
    },
    {
      title: t('user.email'),
      dataIndex: "email",
      key: "age",
    },
    {
      title: t('user.cellphone'),
      dataIndex: "cellphone",
      key: "cellphone",
    },
    {
      title: t('user.createTime'),
      key: "createTime",
      dataIndex: "createTime",
    },
    {
      title: t('user.updateTime'),
      key: "updateTime",
      dataIndex: "updateTime",
    },
    {
      title: t('user.status'),
      key: "disabled",
      dataIndex: "disabled",
      render: (_, record) => (
        <>
          {record.disabled ? <>{t('user.disabled')}</> : <>{t('user.enabled')}</>}
        </>
      ),
    },
    {
      title: t('user.action'),
      key: "action",
      render: (_, record) => (
        <Space size="small">
          <Button type="link" style={{ padding: 0, gap: '4px' }} onClick={() => handleEdit(record)}><EditOutlined />{t('user.edit')}</Button>
          <Popconfirm title={t('user.confirmDisable')} onConfirm={() => handleDisable(record)}>
            <Button type="link" style={{ padding: 0, gap: '4px' }}>{record.disabled ? <><UserAddOutlined /> {t('user.enable')}</> : <><StopOutlined /> {t('user.disable')}</>} </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const formatData = (res: any) => {
    const users: DataType[] = [];
    const records = res.records;
    Object.keys(records).forEach((i) => {
      users.push({
        key: records[i].id,
        name: records[i].name,
        email: records[i].email,
        cellphone: records[i].cellphone,
        passwd: records[i].passwd,
        disabled: records[i].disabled,
        createTime: records[i].createTime,
        updateTime: records[i].updateTime,
      });
    });
    setTabData(users);
    return users;
  };
  const listRequest = useRequest(getUserList, {
    wrapperFun: formatData,
  });

  useEffect(() => {
    listRequest.caller();
  }, [refresh]);

  const items: TabsProps["items"] = [
    {
      key: "key1",
      label: t('user.listTitle'),
      children: "",
    },
  ];

  const onPageChange = (current: number, size: number) => {
    setPageSize(size);
    listRequest.caller({ current: current, size: size });
  };

  const initialValues = {
    id: '',
    name: '',
    email: '',
    cellphone: '',
    passwd: '',
  };

  const newUsers = () => {
    setModalTitle(t('user.newUser'));
    initialValues.name = '';
    initialValues.email = '';
    initialValues.cellphone = '';
    initialValues.passwd = '';
    form.setFieldsValue(initialValues);
    setIsModalOpen(true);
  };

  const reqSave = useRequest(saveOrUpdateUser, {
    wrapperFun: (data: any) => {
      message.success(t('user.saveSuccess'));
      setIsModalOpen(false);
      setRefresh(Math.random);
    },
  });
    
  const handleEdit = (record: DataType) => {
    setModalTitle(t('user.editUser'));
    initialValues.id = record.key;
    initialValues.name = record.name;
    initialValues.email = record.email;
    initialValues.cellphone = record.cellphone;
    initialValues.passwd = record.passwd;    
    form.setFieldsValue(initialValues);
    setIsModalOpen(true);
  };

  const handleDisable = (record: DataType) => {
    reqSave.caller({"id": record.key, "disabled": !record.disabled});
  };

  const onFinish = () => {
    form.validateFields().then((values) => {       
      const params = { ...values};
      reqSave.caller(params);
    });
  };

  return (
    <Spin spinning={listRequest.loading}>
      <div className="module">
        <Form
          form={form}
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          onFinish={onFinish}>
          <Row gutter={[16, 2]}>
            <Col span={6}>
              <Form.Item label={t('user.id')} name="id" style={{ marginBottom: 5 }}>
                <Input />
              </Form.Item>
            </Col>
            <Col span={6}>
              <Form.Item label={t('user.name')} name="name" style={{ marginBottom: 5 }}>
                <Input />
              </Form.Item>
            </Col>
          </Row>           
          <Row gutter={[16, 2]}>
            <Col span={16}></Col>
            <Col span={2} style={{ textAlign: 'right' }}>
              <Button type="primary" htmlType="submit">{t('common.search')}</Button>
            </Col>
          </Row>
        </Form>
      </div>
      <div className="module">
        <Tabs defaultActiveKey="1" items={items} tabBarExtraContent={ 
            <Button type="primary" htmlType="submit" onClick={newUsers}>{t('user.newUser')}</Button>
          }
        />
        <Table size="small" columns={columns} dataSource={tabData} pagination={{ pageSize: pageSize, total: totalCount, onChange: onPageChange }} />
        <Modal title={modalTitle} open={isModalOpen}
          onCancel={() => setIsModalOpen(false)}
          onOk={onFinish}
          cancelText={t('common.cancel')}
          okText={t('common.confirm')}
          width="45%"
          style={{ top: 100, height: '80vh' }}
          bodyStyle={{ maxHeight: '70vh', overflowY: 'auto' }}
        >
          <Card>
            <Form
              form={form}
              labelCol={{ span: 4 }}
              wrapperCol={{ span: 18 }}
              initialValues={initialValues}
              onFinish={onFinish}>
              <Form.Item label="id" name="id" style={{ display: 'none' }}>
                <Input />
              </Form.Item>
              <Form.Item label={t('user.name')} name="name" rules={[{ required: true, message: t('user.namePlaceholder') }]}>
                <Input placeholder={t('user.namePlaceholder')} />
              </Form.Item>
              <Form.Item label={t('user.email')} name="email" rules={[{ required: true, message: t('user.emailPlaceholder') }]}>
                <Input placeholder={t('user.emailPlaceholder')} />
              </Form.Item>
              <Form.Item label={t('user.cellphone')} name="cellphone" rules={[{ required: true, message: t('user.cellphonePlaceholder') }]}>
                <Input placeholder={t('user.cellphonePlaceholder')} />
              </Form.Item>
              <Form.Item label={t('user.password')} name="passwd" rules={[{ required: true, message: t('user.passwordPlaceholder') }]}>
                <Input.Password placeholder={t('user.passwordPlaceholder')} />
              </Form.Item>
            </Form>
          </Card>
        </Modal>
      </div>
    </Spin>
  );
};

const User = memo(S);

export default User;
