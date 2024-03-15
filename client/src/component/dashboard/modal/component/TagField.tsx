import React, {useEffect, useState} from "react";
import {useCookies} from "react-cookie";

interface TagFieldProps {
  tags: string[],
  onChangeTags: (tags: string[]) => void
}

export const TagField: React.FC<TagFieldProps> = ({ tags, onChangeTags }) => {
  const [cookies] = useCookies()
  const [tempTags, setTempTags] = useState<string[]>(tags);

  useEffect(() => {
    fetch('http://localhost:8080/api/v1/task/tag', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': "Bearer " + cookies.token,
      },
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        ////////////////////////////////////////////////////////////////////////
        // test 용
        console.log(data);
        setTempTags(data);
        ////////////////////////////////////////////////////////////////////////
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }, [cookies.token]);

  return (
    <>
      <label htmlFor="tags" className="block text-sm font-medium text-gray-700">Tags</label>
      <select
        name="tags"
        id="tags"
        value={tempTags}
        onChange={(e) => onChangeTags(Array.from(e.target.selectedOptions, option => option.value))}
        className="mt-1 p-1 w-full border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-indigo-500 border"
        multiple // 다중 선택을 위해 multiple 속성 추가
      >
        {tempTags.map(tag => (
          <option key={tag} value={tag}>{tag}</option>
        ))}
      </select>
    </>
  );
};
