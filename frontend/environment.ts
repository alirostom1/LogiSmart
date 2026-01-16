export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v3/',

  // Auth endpoints
  auth: {
    login: 'auth/login',
    register: 'auth/register',
    refresh: 'auth/refresh',
    logout: 'auth/logout',
    oauth2: {
      google: '/oauth2/authorization/google',
      github: '/oauth2/authorization/github',
      auth0: '/oauth2/authorization/auth0'
    }
  },

  // Token configuration
  token: {
    accessTokenKey: 'access_token',
    refreshTokenKey: 'refresh_token',
    tokenType: 'Bearer'
  },

  // Refresh token before expiry (in milliseconds) - 30 seconds before
  tokenRefreshThreshold: 30000
};
