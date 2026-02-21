import { proxy, useSnapshot } from "valtio";
import dayjs, { Dayjs } from "dayjs";

/**
 * Resource type definition
 */
export interface Resource {
  id: string;
  pid: string;
  name: string;
  enName: string;
  type: 'ui' | 'api';
  resKey: string;
  method: string;
  description: string;
  createTime: string;
}

/**
 * Global state store
 */
export const store = proxy<{
  userInfo: any;
  resources: Resource[];
  uiResources: Resource[];  // Cached UI resources for quick access
}>({
  userInfo: null,
  resources: [],
  uiResources: [],
});

/**
 * Set user info
 */
export const setUserInfo = (user: any) => {
  store.userInfo = user;
};

/**
 * Set user resources (call after login)
 */
export const setResources = (resources: Resource[]) => {
  store.resources = resources;
  // Cache UI resources for quick permission checks
  store.uiResources = resources.filter(r => r.type === 'ui');
};

/**
 * Clear all state (call on logout)
 */
export const clearState = () => {
  store.userInfo = null;
  store.resources = [];
  store.uiResources = [];
};

/**
 * Check if user has permission for a UI resource
 * @param resKey - Resource key (e.g., 'menu:user', 'btn:user:add')
 * @returns true if user has permission
 */
export const hasPermission = (resKey: string): boolean => {
  // If no resources loaded, allow access (will be handled by backend)
  if (store.uiResources.length === 0) {
    return true;
  }
  return store.uiResources.some(r => r.resKey === resKey);
};

/**
 * Check if user has permission for multiple UI resources (any one)
 * @param resKeys - Array of resource keys
 * @returns true if user has permission for any of the resources
 */
export const hasAnyPermission = (resKeys: string[]): boolean => {
  if (store.uiResources.length === 0) {
    return true;
  }
  return resKeys.some(key => hasPermission(key));
};

/**
 * Check if user has permission for all UI resources
 * @param resKeys - Array of resource keys
 * @returns true if user has permission for all resources
 */
export const hasAllPermissions = (resKeys: string[]): boolean => {
  if (store.uiResources.length === 0) {
    return true;
  }
  return resKeys.every(key => hasPermission(key));
};

/**
 * Get all menu resources (for sidebar filtering)
 * @returns Array of menu resources
 */
export const getMenuResources = (): Resource[] => {
  return store.uiResources.filter(r => r.resKey.startsWith('menu:'));
};

/**
 * Get all button resources for a specific menu
 * @param menuKey - Menu key prefix (e.g., 'user' for 'btn:user:*')
 * @returns Array of button resources
 */
export const getButtonResources = (menuKey: string): Resource[] => {
  return store.uiResources.filter(r => r.resKey.startsWith(`btn:${menuKey}:`));
};

/**
 * Hook to use store snapshot
 */
export const useStore = () => useSnapshot(store);
