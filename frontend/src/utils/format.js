import moment from 'moment';

export const useFormat = () => {
  const formattedContent = (data) => {
    return data ? data.replace(/\n/g, '<br>') : '';
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };

  const formatDateTime = (date) => {
    const now = moment();  // 현재 날짜와 시간
    const input = moment(date);  // 비교할 날짜

    // 년도가 다른 경우 'YYYY-MM-DD' 형식
    if (!input.isSame(now, 'year')) {
        return input.format('YYYY년 MM월 DD일');
    }

    // 년도는 같고, 월이나 일이 다른 경우 'MM-DD' 형식
    if (!input.isSame(now, 'month') || !input.isSame(now, 'day')) {
        return input.format('MM월 DD일');
    }

    // 년, 월, 일이 모두 같을 경우 'HH:mm A' (AM/PM) 형식
    return input.format('hh:mm A');
  };

  return {
    formattedContent,
    formatDate,
    formatDateTime
  };
}