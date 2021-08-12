export function isvalidUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/* 大小写字母*/
export function validatAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

export function validateAccount(rule, value, callback) {
  if (/^[0-9]+$/.test(value)) {
    callback(new Error("用户名不能全为数字"));
  } else if (value.length < 2) {
    callback(new Error("用户名不能少于2位"));
  } else if (value.length > 10) {
    callback(new Error("用户名不能超过10位"));
  } else {
    callback();
  }
};

export function validatePass(rule, value, callback) {
  if (value.length < 6) {
    callback(new Error("密码不能小于6位"));
  } else {
    if (escape(value).indexOf( "%u" ) >= 0) {
      callback(new Error("密码不能有中文"));
    } else {
      callback();
    }
  }
};

export function validateMobile(rule, value, callback) {
  if (value === "") {
    callback(new Error("请输入手机号码"));
  } else if (!/^1[3456789]\d{9}$/.test(value)) {
    callback(new Error("请输入11位手机号码"));
  } else {
    callback();
  }
};