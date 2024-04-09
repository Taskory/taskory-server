import React, { useEffect, useState } from "react";
import { TagInterface } from "../../../../../interface/TagInterface";
import {getAuthCookie} from "../../../../../util/CookieUtil";

interface TagFieldProps {
  selectedTags: TagInterface[];
  setSelectedTags: (tags: TagInterface[]) => void;
}

export const TagField: React.FC<TagFieldProps> = ({ selectedTags, setSelectedTags }) => {
  const [allTags, setAllTags] = useState<TagInterface[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/task/tag", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + getAuthCookie(),
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setAllTags(data);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  }, []);

  // 체크박스 클릭 시 이벤트 핸들러
  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>, tag: TagInterface) => {
    const isChecked = e.target.checked;
    if (isChecked) {
      // 선택된 태그 리스트에 추가
      setSelectedTags([...selectedTags, tag]);
    } else {
      // 선택된 태그 리스트에서 제거
      setSelectedTags(selectedTags.filter((selectedTag) => selectedTag.id !== tag.id));
    }
  };

  return (
    <>
          <label htmlFor="tags" className="block text-sm font-medium text-gray-700">
            Tags
          </label>
          <details className="dropdown dropdown-top">
            <summary className="m-1 btn btn-xs">Select</summary>
            <ul className="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52">
              {allTags.map((tag) => (
                <div className="form-control" key={tag.id}>
                  <label className="label cursor-pointer">
                    <span className="label-text">{tag.name}</span>
                    <input
                      type="checkbox"
                      className="checkbox"
                      defaultChecked={selectedTags.some((selectedTag) => selectedTag.id === tag.id)}
                      onChange={(e) => handleCheckboxChange(e, tag)}
                    />
                  </label>
                </div>
              ))}
            </ul>
          </details>
          {selectedTags.map((tag) => (
            <p key={tag.id}>#{tag.name}</p>
          ))}
    </>
  );
};
