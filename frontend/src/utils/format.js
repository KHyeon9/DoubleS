import moment from 'moment';

export const useFormat = () => {
  const formattedContent = (data) => {
    return data ? data.replace(/\n/g, '<br>') : '';
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };

  return {
    formattedContent,
    formatDate
  };
}