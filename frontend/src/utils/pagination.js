// src/utils/pagination.js
import { ref, computed } from 'vue';
import moment from 'moment';

export const usePagination = () => {
  const page = ref(0);
  const totalPages = ref(0);

  const nextPage = () => {
    if (page.value < totalPages.value - 1) {
      page.value += 1;
    }
  };

  const prevPage = () => {
    if (page.value > 0) {
      page.value -= 1;
    }
  };

  const goToPage = (pageNumber) => {
    page.value = pageNumber;
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };

  const paginatedPageNumbers = computed(() => {
    const totalPageNumbers = 5;
    let startPage = Math.max(1, page.value + 1 - Math.floor(totalPageNumbers / 2));
    let endPage = Math.min(totalPages.value, startPage + totalPageNumbers - 1);

    if (endPage - startPage < totalPageNumbers - 1) {
      startPage = Math.max(1, endPage - totalPageNumbers + 1);
    }

    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  });

  return {
    page,
    totalPages,
    nextPage,
    prevPage,
    goToPage,
    formatDate,
    paginatedPageNumbers,
  };
};