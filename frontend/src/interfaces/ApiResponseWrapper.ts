export interface ApiResponseWrapper<T> {
  timestamp: string;
  code: number;
  status: string;
  message: string;
  path: string;
  data?: T
}
