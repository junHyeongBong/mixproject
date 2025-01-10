import axios, {AxiosRequestConfig} from 'axios'

const axiosInstance = axios.create({
	baseURL: import.meta.env.VITE_APP_API_BASE_URL || 'https://api.example.com/', //.env 파일에서 기본 API URL읽어오며 없을경우 기본값
	timeout: 30000, //요청 제한 시간 설정(30초)
});

axiosInstance.interceptors.request.use(
	(config) => {
		const token = localStorage.getItem('authToken');
		if(token) {
			config.headers['Authorization'] = `Bearer ${token}`;
		}
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
);

axiosInstance.interceptors.response.use(
	(response) => {
		const authHeader = response.headers['authorization'];
		if(authHeader) {
			const token = authHeader.split(' ')[1];
			localStorage.setItem('authToken', token);
		}
		
		return response;
	},
	(error) => {
		if(error.response?.status === 401) {
			localStorage.removeItem('authToken');
			window.location.href = '/login';    //redirect to login
		}
		return Promise.reject(error);
	}
)

export const getData = async <RES, REQ>(url: string, params?: REQ, config?: AxiosRequestConfig) : Promise<RES> => {
	try {
		let fullUrl = `${url}`;
		if (params) {
			const queryString = new URLSearchParams(params as any).toString();
			fullUrl = `${url}?${queryString}`;
		}
		const response = await axiosInstance.get<IResponse<RES>>(fullUrl, config);
		return response.data;
	} catch (error) {
		throw new Error(error.message);
	}
};

export const getBlobData = async <RES, REQ>(
	url: string,
	params?: REQ,
	config?: AxiosRequestConfig
): Promise<axios.AxiosResponse<RES>> => {
	try {
		let fullUrl = `${url}`;
		if (params) {
			const queryString = new URLSearchParams(params as any).toString();
			fullUrl = `${url}?${queryString}`;
		}
		return await axiosInstance.get<Blob>(fullUrl, config);
	} catch (error) {
		throw new Error((error as Error).message);
	}
};

export const getHead = async <RES, REQ>(url: string, params?: REQ, config?: AxiosRequestConfig) : Promise<axios.AxiosResponse<RES>> => {
	try {
		let fullUrl = `${url}`;
		if (params) {
			const queryString = new URLSearchParams(params as any).toString();
			fullUrl = `${url}?${queryString}`;
		}
		return await axiosInstance.head<IResponse<RES>>(fullUrl, config);
	} catch (error) {
		console.error(error);
		throw new Error((error as Error).message);
	}
};

export const getPagingData = async <RES, REQ>(url: string, params?: REQ, config?: AxiosRequestConfig): Promise<RES> => {
	try {
		let fullUrl = `${url}`;
		
		if (params) {
			const flattenedParams: Record<string, any> = {};
			
			const pagingParams = params as unknown as IPagingRequest<any>;
			
			if(pagingParams.page !== undefined) flattenedParams['page'] = pagingParams.page;
			if(pagingParams.size !== undefined) flattenedParams['size'] = pagingParams.size;
			if(pagingParams.sort !== undefined) flattenedParams['sort'] = pagingParams.sort;
			
			if(pagingParams.body) {
				Object.entries(pagingParams.body).forEach(([key, value]) => {
					if(value !== undefined && value !== null) {
						flattenedParams[key] = value;
					}
				});
			}
			
			const queryString = new URLSearchParams(flattenedParams as any).toString();
			fullUrl = `${url}?${queryString}`;
		}
		
		const response = await axiosInstance.get<IResponse<RES>>(fullUrl, config);
		return response.data;
	}catch (error) {
		throw new Error((error as Error).message);
	}
};

export const postData = async <RES, REQ>(url: string, data?: REQ, config?: AxiosRequestConfig) : Promise<RES> => {
	try {
		const response = await axiosInstance.post<IResponse<RES>>(url, data, config);
		return response.data;
	} catch (error) {
		console.log(error)
		throw new Error((error as Error).message);
	}
};

export const putData = async <RES, REQ>(url: string, data?: REQ, config?: AxiosRequestConfig) : Promise<RES> => {
	try {
		const response = await axiosInstance.put<IResponse<RES>>(url, data, config);
		return response.data;
	} catch (error) {
		throw new Error((error as Error).message);
	}
};

export const deleteData = async <RES, REQ>(url: string, params?: REQ, config?: AxiosRequestConfig) : Promise<RES> => {
	try {
		let fullUrl = `${url}`;
		if (params) {
			const queryString = new URLSearchParams(params as any).toString();
			fullUrl = `${url}?${queryString}`;
		}
		const response = await axiosInstance.delete<IResponse<RES>>(fullUrl, config);
		return response.data;
	} catch (error) {
		throw new Error((error as Error).message);
	}
	
}

interface IHeader {
	fileName?: string;
	contentDisposition?: string;
	contentType: string;
	contentLength: number;
	date?: string;
}

interface IResponse<T> {
	code: number;
	message: string;
	resData?: T;
	systemMessage?: string;
}

interface IRequest<T> {
	body?: T;
}

interface IPagingRequest<T> {
	page: number;
	size: number;
	sort?: string[];
	body?: T;
}

interface PagingContent<T> {
	totalPages: number;
	totalElements: number;
	first: boolean;
	last: boolean;
	sort: {
		sorted: boolean;
		unsorted: boolean;
		empty: boolean;
	};
	pageable: {
		offset: number;
		pageNumber: number;
		pageSize: number;
		paged: boolean;
		unpaged: boolean
	}
	numberOfElements: number;
	size: number;
	empty: boolean;
	content?: T[]
}

interface IPagingResponse<T> {
	code: number;
	message: string;
	resData?: PagingContent<T>;
}

const defaultResponse = {
	code: 400,
	message: "유효하지 않은 통신입니다.",
}

export default axiosInstance;
export {defaultResponse}
export type {
	IRequest,
	IResponse,
	IPagingRequest,
	IPagingResponse,
	IHeader,
	PagingContent,
}




