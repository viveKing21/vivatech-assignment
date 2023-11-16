class CookieHelper {
    static cookies = null;
  
    static parseCookies() {
      const cookieObject = {};
      const cookies = document.cookie.split(';');
      
      for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        const [name, value] = cookie.split('=');
        cookieObject[name] = value;
      }
      return cookieObject;
    }
  
    static getCookie(name) {
      if (!CookieHelper.cookies) {
        CookieHelper.cookies = CookieHelper.parseCookies();
      }
      return CookieHelper.cookies[name] || null;
    }
  
    static setCookie(name, value, daysToExpire) {
      let cookieString = `${name}=${value};`;
  
      if (daysToExpire) {
        const expirationDate = new Date();
        expirationDate.setTime(expirationDate.getTime() + daysToExpire * 24 * 60 * 60 * 1000);
        cookieString += `expires=${expirationDate.toUTCString()};`;
      }
  
      document.cookie = cookieString;
      CookieHelper.cookies = CookieHelper.parseCookies();
    }
  
    static removeCookie(name) {
      document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC;`;
      CookieHelper.cookies = CookieHelper.parseCookies();
    }
  }
  
  export default CookieHelper;
  